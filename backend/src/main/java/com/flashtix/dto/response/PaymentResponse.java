package com.flashtix.dto.response;

import lombok.Builder;

@Builder
public record PaymentResponse(
        String orderCode,
        String bookingCode,
        Integer quantity,
        String paymentUrl) {
}
