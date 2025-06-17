package com.shashi.paymentservice.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {
    private Long userId;
    private Long reservationId;
    private double amount;
    private String method;       // "CARD", "CASH", etc.
    private String cardNumber;   // mock validation only if method is "CARD"
}
