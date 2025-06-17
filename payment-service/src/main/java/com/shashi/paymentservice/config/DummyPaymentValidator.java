package com.shashi.paymentservice.config;

import org.springframework.stereotype.Component;

@Component
public class DummyPaymentValidator {
    public boolean isValidCard(String cardNumber) {
        return cardNumber != null && cardNumber.length() == 16 && cardNumber.startsWith("4");
    }
}