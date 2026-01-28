package com.flashtix.dto.request;

import lombok.Builder;

@Builder
public record MoMoPaymentRequest(
                String partnerCode,
                String accessKey,
                String orderId,
                String requestId,
                String lang,
                Long amount,
                String orderInfo,
                String requestType,
                String redirectUrl,
                String ipnUrl,
                String extraData,
                String signature) {

}
