package com.flashtix.service.impl;

import com.flashtix.common.enums.OrderStatus;
import com.flashtix.common.enums.TicketStatus;
import com.flashtix.dto.request.CheckInRequest;
import com.flashtix.dto.response.CheckInResponse;
import com.flashtix.dto.response.EventResponse;
import com.flashtix.dto.response.OrderDetailResponse;
import com.flashtix.dto.response.TicketResponse;
import com.flashtix.entity.Event;
import com.flashtix.entity.Order;
import com.flashtix.entity.Ticket;
import com.flashtix.repository.OrderRepository;
import com.flashtix.repository.TicketRepository;
import com.flashtix.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional(readOnly = true) // PERFORMANCE: Read-only optimization
    public OrderDetailResponse getOrderByBookingCode(String bookingCode) {
        Order order = orderRepository.findByBookingCode(bookingCode);
        if (order == null) {
            throw new RuntimeException("Booking code not found: " + bookingCode);
        }

        // Fetch tickets for this order
        List<Ticket> tickets = ticketRepository.findByOrder_BookingCode(bookingCode);

        // Map tickets to response
        List<TicketResponse> ticketResponses = tickets.stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());

        // Build event response
        Event event = order.getTicketType().getEvent();
        EventResponse eventResponse = new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getStartTime(),
                event.getLocation(),
                event.getBannerUrl(),
                null // Don't include all ticket types
        );

        return OrderDetailResponse.builder()
                .orderId(order.getId())
                .bookingCode(order.getBookingCode())
                .orderCode(order.getOrderCode())
                .customerName(order.getCustomerName())
                .customerEmail(order.getCustomerEmail())
                .customerPhone(order.getCustomerPhone())
                .event(eventResponse)
                .ticketTypeName(order.getTicketType().getName())
                .ticketPrice(order.getTicketType().getPrice())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .orderStatus(order.getStatus())
                .createdAt(order.getCreatedAt())
                .expiredAt(order.getExpiredAt())
                .tickets(ticketResponses)
                .build();
    }

    @Override
    @Transactional
    public CheckInResponse checkInTickets(CheckInRequest request) {
        LocalDateTime checkInTime = LocalDateTime.now();

        // Validate booking code exists
        Order order = orderRepository.findByBookingCode(request.bookingCode());
        if (order == null) {
            throw new RuntimeException("Booking code not found: " + request.bookingCode());
        }

        // Check order is PAID
        if (order.getStatus() != OrderStatus.PAID) {
            throw new RuntimeException("Cannot check in tickets for order with status: " + order.getStatus());
        }

        // Fetch ALL tickets for this booking ONCE (avoid redundant query later)
        List<Ticket> allTickets = ticketRepository.findByOrder_BookingCode(request.bookingCode());

        List<Ticket> ticketsToCheckIn;

        // Determine which tickets to check in
        if (request.ticketCodes() == null || request.ticketCodes().isEmpty()) {
            // Check in all ACTIVE tickets
            ticketsToCheckIn = allTickets.stream()
                    .filter(t -> t.getStatus() == TicketStatus.ACTIVE)
                    .collect(Collectors.toList());
        } else {
            // Check in specific tickets - use JOIN FETCH to avoid N+1
            ticketsToCheckIn = ticketRepository.findByTicketCodeInWithOrder(request.ticketCodes());

            // Validate all requested tickets belong to this booking
            String bookingCode = request.bookingCode();
            for (Ticket ticket : ticketsToCheckIn) {
                if (!ticket.getOrder().getBookingCode().equals(bookingCode)) {
                    throw new RuntimeException("Ticket " + ticket.getTicketCode() +
                            " does not belong to booking " + bookingCode);
                }
            }
        }

        int alreadyCheckedIn = 0;
        List<String> errorMessages = new ArrayList<>();
        List<String> activeTicketCodes = new ArrayList<>();

        // Separate tickets by status
        for (Ticket ticket : ticketsToCheckIn) {
            if (ticket.getStatus() == TicketStatus.CHECKED_IN) {
                alreadyCheckedIn++;
                errorMessages.add("Ticket " + ticket.getTicketCode() + " already used");
            } else if (ticket.getStatus() == TicketStatus.ACTIVE) {
                activeTicketCodes.add(ticket.getTicketCode());
            } else {
                errorMessages.add("Ticket " + ticket.getTicketCode() + " has invalid status: " + ticket.getStatus());
            }
        }

        // If any tickets were already checked in and specific tickets were requested,
        // throw error
        if (alreadyCheckedIn > 0 && request.ticketCodes() != null && !request.ticketCodes().isEmpty()) {
            throw new RuntimeException(String.join(", ", errorMessages));
        }

        int checkedInCount = 0;

        // PERFORMANCE OPTIMIZATION: Batch update instead of individual saves
        if (!activeTicketCodes.isEmpty()) {
            checkedInCount = ticketRepository.bulkUpdateTicketStatus(
                    activeTicketCodes,
                    TicketStatus.CHECKED_IN,
                    checkInTime,
                    request.validatedBy());

            // Refresh tickets in memory to reflect changes
            allTickets = ticketRepository.findByOrder_BookingCode(request.bookingCode());
        }

        List<TicketResponse> ticketResponses = allTickets.stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());

        String message = String.format("Successfully checked in %d ticket(s)", checkedInCount);
        if (alreadyCheckedIn > 0) {
            message += String.format(" (%d already checked in)", alreadyCheckedIn);
        }

        log.info("Check-in for booking {}: {} new, {} already checked in",
                request.bookingCode(), checkedInCount, alreadyCheckedIn);

        return CheckInResponse.builder()
                .bookingCode(request.bookingCode())
                .totalTickets(allTickets.size())
                .checkedInCount(checkedInCount)
                .previouslyCheckedIn(alreadyCheckedIn)
                .tickets(ticketResponses)
                .message(message)
                .build();
    }

    @Override
    @Transactional(readOnly = true) // PERFORMANCE: Read-only optimization
    public TicketResponse getTicketByCode(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + ticketCode));

        return mapToTicketResponse(ticket);
    }

    private TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .status(ticket.getStatus())
                .createdAt(ticket.getCreatedAt())
                .checkedInAt(ticket.getCheckedInAt())
                .checkedInBy(ticket.getCheckedInBy())
                .build();
    }
}
