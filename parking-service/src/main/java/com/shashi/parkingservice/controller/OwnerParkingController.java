package com.shashi.parkingservice.controller;

import com.shashi.parkingservice.dto.ParkingSpotDto;
import com.shashi.parkingservice.model.ParkingSpot;
import com.shashi.parkingservice.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner/parking")
@RequiredArgsConstructor
public class OwnerParkingController {

    private final ParkingService parkingService;

    @GetMapping("/spots/{ownerId}")
    public ResponseEntity<List<ParkingSpot>> getSpotsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(parkingService.getSpotsByOwner(ownerId));
    }

    @PostMapping("/spots")
    public ResponseEntity<ParkingSpot> addSpot(@RequestBody ParkingSpotDto dto) {
        return ResponseEntity.ok(parkingService.addSpot(dto));
    }

    @PutMapping("/spots/{spotId}")
    public ResponseEntity<ParkingSpot> updateSpot(
            @PathVariable Long spotId,
            @RequestBody ParkingSpotDto dto) {
        return ResponseEntity.ok(parkingService.updateSpot(spotId, dto));
    }

    @DeleteMapping("/spots/{spotId}")
    public ResponseEntity<Void> deleteSpot(@PathVariable Long spotId) {
        parkingService.deleteSpot(spotId);
        return ResponseEntity.ok().build();
    }
}
