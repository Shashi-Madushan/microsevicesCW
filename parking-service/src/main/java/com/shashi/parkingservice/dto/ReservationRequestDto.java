package com.shashi.parkingservice.dto;

import lombok.Data;

@Data
public class ReservationRequestDto {
    private Long userId;
    private Long parkingSpotId;
    private Double amount;
    private String paymentMethod;
}