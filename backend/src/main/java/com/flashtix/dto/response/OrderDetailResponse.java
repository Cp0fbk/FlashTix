package com.flashtix.dto.response;

import com.flashtix.common.enums.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderDetailResponse(
        Long orderId,
        String bookingCode,
        String orderCode,
        String customerName,
        String customerEmail,
        String customerPhone,
        EventResponse event,
        String ticketTypeName,
        Double ticketPrice,
        Integer quantity,
        Double totalAmount,
        OrderStatus orderStatus,
        LocalDateTime createdAt,
        LocalDateTime expiredAt,
        List<TicketResponse> tickets) {
}
