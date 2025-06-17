package com.shashi.parkingservice.dto;



import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingSpotDto {
    private String location;
    private String zone;
    private boolean available;
    private String type;
    private Long ownerId;
}
