package com.flashtix.service;

import com.flashtix.dto.request.OrderRequest;
import com.flashtix.dto.response.PaymentResponse;

public interface OrderService {
    PaymentResponse createOrder(OrderRequest request);

    void handlePaymentSuccess(String orderCode, Long transactionId);

    void cancelExpiredOrders();
}
