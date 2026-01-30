package com.flashtix.service;

import com.flashtix.entity.Order;
import com.flashtix.entity.Payment;

public interface EmailService {
    void sendPaymentConfirmationEmail(Order order, Payment payment);
}
