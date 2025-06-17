package com.shashi.parkingservice.dto;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingReservationDto {
    private Long userId;
    private Long parkingSpotId;
}
