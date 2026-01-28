package com.flashtix.controller;

import com.flashtix.common.dto.ApiResponse;
import com.flashtix.dto.request.MoMoIPNRequest;
import com.flashtix.dto.request.OrderRequest;
import com.flashtix.dto.response.PaymentResponse;
import com.flashtix.service.OrderService;
import com.flashtix.service.PaymentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Payment Controller", description = "APIs for payment processing and MoMo integration")
public class PaymentController {

    private final OrderService orderService;
    private final PaymentService moMoService;

    @PostMapping("/create")
    @Operation(summary = "Create order and payment", description = "Create an order and generate MoMo payment URL")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(@Valid @RequestBody OrderRequest request) {
        PaymentResponse paymentResponse = orderService.createOrder(request);
        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), "Payment created successfully", paymentResponse));
    }

    @PostMapping("/callback")
    @Operation(summary = "Payment redirect callback", description = "Handles user redirect after MoMo payment (for UX only, not payment processing)")
    public ResponseEntity<ApiResponse<Void>> paymentCallback(
            @RequestParam String orderCode,
            @RequestParam(required = false) Integer resultCode) {

        log.info("Payment redirect received for order: {} with result code: {}", orderCode, resultCode);

        String message;
        if (resultCode != null && resultCode == 0) {
            message = "Payment processing. Please wait for confirmation.";
        } else {
            message = "Payment status: " + (resultCode != null ? resultCode : "pending");
        }

        return ResponseEntity.ok(
                new ApiResponse<>(HttpStatus.OK.value(), message, null));
    }

    @PostMapping("/momo/ipn")
    @Operation(summary = "MoMo IPN callback", description = "Handles MoMo Instant Payment Notification (server-to-server). Returns MoMo-specific response format.")
    public ResponseEntity<Map<String, Object>> handleMoMoIPN(@Valid @RequestBody MoMoIPNRequest ipnRequest) {
        log.info("Received MoMo IPN: {}", ipnRequest);
        // Verify signature
        if (!moMoService.verifySignature(ipnRequest)) {
            log.warn("Invalid signature for order: {}", ipnRequest.orderId());
            return ResponseEntity.ok(Map.of("resultCode", 97, "message", "Invalid signature"));
        }
        // Check payment success
        if (ipnRequest.resultCode() == 0) {
            orderService.handlePaymentSuccess(ipnRequest.orderId(), ipnRequest.transId());
            return ResponseEntity.ok(Map.of("resultCode", 0, "message", "Success"));
        } else {
            log.warn("Payment failed for order {}: {}", ipnRequest.orderId(), ipnRequest.message());
            return ResponseEntity.ok(Map.of("resultCode", 0, "message", "Acknowledged"));
        }
    }
}
