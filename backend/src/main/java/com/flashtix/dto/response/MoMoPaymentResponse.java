package com.flashtix.dto.response;

public record MoMoPaymentResponse(
        String partnerCode,
        String orderId,
        String requestId,
        Long amount,
        Long transId,
        Integer resultCode,
        String message,
        String payUrl,
        String deeplink,
        String qrCodeUrl) {

}
