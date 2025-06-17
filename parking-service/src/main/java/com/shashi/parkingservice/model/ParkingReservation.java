package com.shashi.parkingservice.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;          // Driver
    private Long parkingSpotId;

    private LocalDateTime reservedAt;
    private LocalDateTime releasedAt;
    private String status;        // ACTIVE, COMPLETED, CANCELLED
}
