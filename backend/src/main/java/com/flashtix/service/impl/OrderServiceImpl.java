package com.flashtix.service.impl;

import com.flashtix.common.enums.OrderStatus;
import com.flashtix.common.enums.PaymentStatus;
import com.flashtix.dto.request.OrderRequest;
import com.flashtix.dto.response.MoMoPaymentResponse;
import com.flashtix.dto.response.PaymentResponse;
import com.flashtix.entity.Order;
import com.flashtix.entity.Payment;
import com.flashtix.entity.TicketType;
import com.flashtix.repository.OrderRepository;
import com.flashtix.repository.PaymentRepository;
import com.flashtix.repository.TicketTypeRepository;
import com.flashtix.service.OrderService;
import com.flashtix.service.PaymentService;
import com.flashtix.service.TicketInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final TicketInventoryService ticketInventoryService;
    private final OrderRepository orderRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentService moMoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse createOrder(OrderRequest request) {
        ticketInventoryService.decreaseStock(request.ticketTypeId(), request.quantity());
        try {
            TicketType ticketType = ticketTypeRepository.findById(request.ticketTypeId())
                    .orElseThrow(() -> new RuntimeException("Ticket Type not found"));
            Order order = Order.builder()
                    .orderCode(UUID.randomUUID().toString())
                    .customerName(request.customerName())
                    .customerEmail(request.customerEmail())
                    .customerPhone(request.customerPhone())
                    .ticketType(ticketType)
                    .quantity(request.quantity())
                    .totalAmount(ticketType.getPrice() * request.quantity())
                    .status(OrderStatus.PENDING)
                    .expiredAt(LocalDateTime.now().plusMinutes(15))
                    .build();
            orderRepository.save(order);
            // ** INTEGRATE MOMO HERE **
            String orderInfo = String.format("Ticket: %s x%d", ticketType.getName(), request.quantity());
            MoMoPaymentResponse momoResponse = moMoService.createPayment(
                    order.getOrderCode(),
                    order.getTotalAmount().longValue(),
                    orderInfo,
                    request.paymentMethod());
            if (momoResponse.resultCode() != 0) {
                throw new RuntimeException("MoMo payment creation failed: " + momoResponse.message());
            }
            return PaymentResponse.builder()
                    .orderCode(order.getOrderCode())
                    .paymentUrl(momoResponse.payUrl()) // Real MoMo payment URL
                    .build();
        } catch (Exception e) {
            log.error("Order creation failed", e);
            ticketInventoryService.revertStock(request.ticketTypeId(), request.quantity());
            throw e;
        }
    }

    @Override
    @Transactional
    public void handlePaymentSuccess(String orderCode, Long transactionId) {
        Order order = orderRepository.findByOrderCode(orderCode);
        if (order == null) {
            throw new RuntimeException("Order not found");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            log.info("Order {} already processed with status: {}", orderCode, order.getStatus());
            return; // Idempotency
        }

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // Update database remaining_quantity to sync with Redis
        TicketType ticketType = order.getTicketType();
        int newRemainingQuantity = ticketType.getRemainingQuantity() - order.getQuantity();

        if (newRemainingQuantity < 0) {
            log.warn("Database remaining quantity for ticket type {} went negative: {}",
                    ticketType.getId(), newRemainingQuantity);
            newRemainingQuantity = 0;
        }

        ticketType.setRemainingQuantity(newRemainingQuantity);
        ticketTypeRepository.save(ticketType);

        log.info("Updated ticket type {} remaining quantity: {} -> {}",
                ticketType.getId(),
                ticketType.getRemainingQuantity() + order.getQuantity(),
                newRemainingQuantity);

        // Record Payment with actual transaction ID from MoMo if available
        String transactionCode = transactionId != null
                ? String.valueOf(transactionId)
                : UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .amount(order.getTotalAmount())
                .paymentProvider("MOMO")
                .transactionCode(transactionCode)
                .status(PaymentStatus.SUCCESS)
                .paymentTime(LocalDateTime.now())
                .order(order)
                .build();

        paymentRepository.save(payment);

        // TODO: Send Email Async
        log.info("Order {} paid successfully with transaction ID: {}", orderCode, transactionCode);
    }

    @Override
    @Transactional
    public void cancelExpiredOrders() {
        List<Order> expiredOrders = orderRepository.findByStatusAndExpiredAtBefore(OrderStatus.PENDING,
                LocalDateTime.now());

        for (Order order : expiredOrders) {
            try {
                log.info("Cancelling expired order: {}", order.getOrderCode());
                order.setStatus(OrderStatus.CANCELLED);
                orderRepository.save(order);

                // Release Stock
                ticketInventoryService.revertStock(order.getTicketType().getId(), order.getQuantity());
            } catch (Exception e) {
                log.error("Failed to cancel order {}", order.getOrderCode(), e);
            }
        }
    }
}
