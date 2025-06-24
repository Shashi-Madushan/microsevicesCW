package com.shashi.parkingservice.controller;

import com.shashi.parkingservice.model.ParkingReservation;
import com.shashi.parkingservice.model.ParkingSpot;
import com.shashi.parkingservice.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/parking")
@RequiredArgsConstructor
public class AdminParkingController {

    private final ParkingService parkingService;

    @GetMapping("/spots")
    public ResponseEntity<List<ParkingSpot>> getAllSpots() {
        return ResponseEntity.ok(parkingService.getAllSpots());
    }

    @GetMapping("/reservations")
    public ResponseEntity<List<ParkingReservation>> getAllReservations() {
        return ResponseEntity.ok(parkingService.getAllReservations());
    }

    @GetMapping("/spots/type/{type}")
    public ResponseEntity<List<ParkingSpot>> getSpotsByType(@PathVariable String type) {
        return ResponseEntity.ok(parkingService.getSpotsByType(type));
    }

    @GetMapping("/spots/filter")
    public ResponseEntity<List<ParkingSpot>> getSpotsByTypeAndZone(
            @RequestParam String type,
            @RequestParam String zone) {
        return ResponseEntity.ok(parkingService.getSpotsByTypeAndZone(type, zone));
    }
}
