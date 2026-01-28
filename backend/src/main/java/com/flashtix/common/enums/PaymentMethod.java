package com.flashtix.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
    MOMO_WALLET("captureWallet", "MoMo Wallet/QR Code"),
    CREDIT_CARD("payWithCC", "Credit Card (Visa/Master/JCB)"),
    ATM_CARD("payWithATM", "ATM Card");

    private final String requestType;
    private final String displayName;
}
