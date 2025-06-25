package com.shashi.parkingservice.model;

import lombok.Data;

@Data
public class Payment {
    private Long id;
    private Long userId;
    private Long reservationId;
    private Double amount;
    private String method;
    private String status;
}
