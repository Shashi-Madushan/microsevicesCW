package com.shashi.parkingservice.service;


import com.shashi.parkingservice.dto.ParkingReservationDto;
import com.shashi.parkingservice.dto.ParkingSpotDto;
import com.shashi.parkingservice.model.ParkingReservation;
import com.shashi.parkingservice.model.ParkingSpot;
import com.shashi.parkingservice.repository.ParkingReservationRepository;
import com.shashi.parkingservice.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingSpotRepository spotRepo;
    private final ParkingReservationRepository reservationRepo;

    public ParkingSpot addSpot(ParkingSpotDto dto) {
        ParkingSpot spot = ParkingSpot.builder()
                .location(dto.getLocation())
                .zone(dto.getZone())
                .type(dto.getType())
                .available(dto.isAvailable())
                .ownerId(dto.getOwnerId())
                .build();
        return spotRepo.save(spot);
    }

    public List<ParkingSpot> getAvailableSpots() {
        return spotRepo.findByAvailableTrue();
    }

    public List<ParkingSpot> getSpotsByZone(String zone) {
        return spotRepo.findByZone(zone);
    }

    public ParkingReservation reserveSpot(ParkingReservationDto dto) {
        ParkingSpot spot = spotRepo.findById(dto.getParkingSpotId())
                .orElseThrow(() -> new RuntimeException("Spot not found"));
        if (!spot.isAvailable()) {
            throw new RuntimeException("Spot is not available");
        }
        spot.setAvailable(false);
        spotRepo.save(spot);

        ParkingReservation reservation = ParkingReservation.builder()
                .userId(dto.getUserId())
                .parkingSpotId(dto.getParkingSpotId())
                .reservedAt(LocalDateTime.now())
                .status("ACTIVE")
                .build();
        return reservationRepo.save(reservation);
    }

    public ParkingReservation releaseSpot(Long reservationId) {
        ParkingReservation res = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        res.setStatus("COMPLETED");
        res.setReleasedAt(LocalDateTime.now());

        ParkingSpot spot = spotRepo.findById(res.getParkingSpotId())
                .orElseThrow(() -> new RuntimeException("Spot not found"));
        spot.setAvailable(true);
        spotRepo.save(spot);

        return reservationRepo.save(res);
    }

    // Get reservation history by user ID
    public List<ParkingReservation> getReservationHistoryByUserId(Long userId) {
        return reservationRepo.findByUserId(userId);
    }
}