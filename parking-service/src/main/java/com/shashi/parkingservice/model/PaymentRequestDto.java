package com.shashi.parkingservice.model;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private Long userId;
    private Long reservationId;
    private Double amount;
    private String method;
}
