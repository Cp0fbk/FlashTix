package com.flashtix.service.impl;

import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.flashtix.common.enums.PaymentMethod;
import com.flashtix.config.MoMoConfig;
import com.flashtix.dto.request.MoMoIPNRequest;
import com.flashtix.dto.request.MoMoPaymentRequest;
import com.flashtix.dto.response.MoMoPaymentResponse;
import com.flashtix.service.PaymentService;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MoMoServiceImpl implements PaymentService {
    private final MoMoConfig moMoConfig;
    private final Gson gson = new Gson();

    @Override
    public MoMoPaymentResponse createPayment(String orderCode, Long amount, String orderInfo,
            PaymentMethod paymentMethod) {
        try {
            String requestId = UUID.randomUUID().toString();
            String extraData = "";

            // Get request type from payment method enum
            String requestType = paymentMethod.getRequestType();

            // Build raw signature data
            String rawData = String.format(
                    "accessKey=%s&amount=%d&extraData=%s&ipnUrl=%s&orderId=%s&orderInfo=%s&partnerCode=%s&redirectUrl=%s&requestId=%s&requestType=%s",
                    moMoConfig.getAccessKey(),
                    amount,
                    extraData,
                    moMoConfig.getIpnUrl(),
                    orderCode,
                    orderInfo,
                    moMoConfig.getPartnerCode(),
                    moMoConfig.getRedirectUrl(),
                    requestId,
                    requestType);
            // Generate HMAC SHA256 signature
            String signature = signHmacSHA256(rawData, moMoConfig.getSecretKey());
            // Create request payload
            MoMoPaymentRequest request = MoMoPaymentRequest.builder()
                    .partnerCode(moMoConfig.getPartnerCode())
                    .accessKey(moMoConfig.getAccessKey())
                    .orderId(orderCode)
                    .requestId(requestId)
                    .lang("vi")
                    .orderInfo(orderInfo)
                    .amount(amount)
                    .requestType(requestType)
                    .redirectUrl(moMoConfig.getRedirectUrl())
                    .ipnUrl(moMoConfig.getIpnUrl())
                    .extraData(extraData)
                    .signature(signature)
                    .build();
            // Send HTTP POST to MoMo
            String endpoint = moMoConfig.getEndpoint() + moMoConfig.getCreateUrl();
            String jsonPayload = gson.toJson(request);

            log.info("Creating MoMo payment with method: {} (requestType: {})", paymentMethod.getDisplayName(),
                    requestType);
            log.info("MoMo Payment Request: {}", jsonPayload);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(endpoint);
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonPayload, "UTF-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            String responseBody = EntityUtils.toString(response.getEntity());

            log.info("MoMo Payment Response: {}", responseBody);
            return gson.fromJson(responseBody, MoMoPaymentResponse.class);
        } catch (Exception e) {
            log.error("Error creating MoMo payment", e);
            throw new RuntimeException("Failed to create MoMo payment");
        }
    }

    @Override
    public boolean verifySignature(MoMoIPNRequest ipnRequest) {
        try {
            String rawData = String.format(
                    "accessKey=%s&amount=%d&extraData=%s&message=%s&orderId=%s&orderInfo=%s&orderType=%s&partnerCode=%s&payType=%s&requestId=%s&responseTime=%d&resultCode=%d&transId=%d",
                    moMoConfig.getAccessKey(),
                    ipnRequest.amount(),
                    ipnRequest.extraData(),
                    ipnRequest.message(),
                    ipnRequest.orderId(),
                    ipnRequest.orderInfo(),
                    ipnRequest.orderType(),
                    ipnRequest.partnerCode(),
                    ipnRequest.payType(),
                    ipnRequest.requestId(),
                    ipnRequest.responseTime(),
                    ipnRequest.resultCode(),
                    ipnRequest.transId());
            String expectedSignature = signHmacSHA256(rawData, moMoConfig.getSecretKey());
            return expectedSignature.equals(ipnRequest.signature());
        } catch (Exception e) {
            log.error("Error verifying signature", e);
            return false;
        }
    }

    private String signHmacSHA256(String data, String secretKey) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));

        StringBuilder sb = new StringBuilder();
        for (byte b : rawHmac) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
