package com.flashtix.service;

import com.flashtix.common.enums.PaymentMethod;
import com.flashtix.dto.request.MoMoIPNRequest;
import com.flashtix.dto.response.MoMoPaymentResponse;

public interface PaymentService {
    MoMoPaymentResponse createPayment(String orderCode, Long amount, String orderInfo, PaymentMethod paymentMethod);

    boolean verifySignature(MoMoIPNRequest ipnRequest);
}
