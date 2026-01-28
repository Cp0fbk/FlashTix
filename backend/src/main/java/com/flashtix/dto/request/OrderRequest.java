package com.flashtix.dto.request;

import com.flashtix.common.enums.PaymentMethod;

public record OrderRequest(
        Long ticketTypeId,
        Integer quantity,
        String customerName,
        String customerEmail,
        String customerPhone,
        PaymentMethod paymentMethod) {

    // Constructor with default payment method
    public OrderRequest {
        if (paymentMethod == null) {
            paymentMethod = PaymentMethod.MOMO_WALLET;
        }
    }
}
