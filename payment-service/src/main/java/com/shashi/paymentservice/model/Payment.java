package com.shashi.paymentservice.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long reservationId;

    private double amount;
    private String method;     // e.g. "CARD", "CASH", "UPI"
    private String status;     // "SUCCESS", "FAILED"

    private LocalDateTime paidAt;
    private String transactionId;
}
