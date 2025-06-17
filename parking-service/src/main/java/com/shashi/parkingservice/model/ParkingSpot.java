package com.shashi.parkingservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private String zone;
    private boolean available;
    private String type;       // e.g. "Covered", "Open"
    private Long ownerId;      // Logical foreign key to user-service
}
