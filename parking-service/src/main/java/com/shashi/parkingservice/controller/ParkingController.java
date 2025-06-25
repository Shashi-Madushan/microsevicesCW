package com.shashi.parkingservice.controller;


import com.shashi.parkingservice.dto.ParkingReservationDto;
import com.shashi.parkingservice.dto.ParkingSpotDto;
import com.shashi.parkingservice.dto.ReservationRequestDto;
import com.shashi.parkingservice.model.ParkingReservation;
import com.shashi.parkingservice.model.ParkingSpot;
import com.shashi.parkingservice.service.ParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping("/spots")
    public ResponseEntity<ParkingSpot> addSpot(@RequestBody ParkingSpotDto dto) {
        return ResponseEntity.ok(parkingService.addSpot(dto));
    }

    @GetMapping("/spots/available")
    public ResponseEntity<List<ParkingSpot>> getAvailableSpots() {
        return ResponseEntity.ok(parkingService.getAvailableSpots());
    }

    @GetMapping("/spots/zone/{zone}")
    public ResponseEntity<List<ParkingSpot>> getByZone(@PathVariable String zone) {
        return ResponseEntity.ok(parkingService.getSpotsByZone(zone));
    }

    @PostMapping("/reserve")
    public ResponseEntity<ParkingReservation> reserve(@RequestBody ParkingReservationDto dto) {
        return ResponseEntity.ok(parkingService.reserveSpot(dto));
    }

    @PutMapping("/release/{reservationId}")
    public ResponseEntity<ParkingReservation> release(@PathVariable Long reservationId) {
        return ResponseEntity.ok(parkingService.releaseSpot(reservationId));
    }

    @GetMapping("/reservations/user/{userId}")
    public ResponseEntity<List<ParkingReservation>> getReservationHistoryByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(parkingService.getReservationHistoryByUserId(userId));
    }

    @PostMapping("/reserve/with-payment")
    public ResponseEntity<ParkingReservation> reserveWithPayment(
            @RequestBody ReservationRequestDto requestDto) {
        return ResponseEntity.ok(parkingService.reserveSpotAndPay(requestDto));
    }


}