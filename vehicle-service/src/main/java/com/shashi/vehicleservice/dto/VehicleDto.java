package com.shashi.vehicleservice.dto;



import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDto {
    private String licensePlate;
    private String model;
    private String color;
    private Long userId;
}
