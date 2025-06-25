package com.shashi.parkingservice.service;


import com.shashi.parkingservice.clients.PaymentClient;
import com.shashi.parkingservice.clients.UserClient;
import com.shashi.parkingservice.dto.ParkingReservationDto;
import com.shashi.parkingservice.dto.ParkingSpotDto;
import com.shashi.parkingservice.dto.ReservationRequestDto;
import com.shashi.parkingservice.model.*;
import com.shashi.parkingservice.repository.ParkingReservationRepository;
import com.shashi.parkingservice.repository.ParkingSpotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParkingService {
    private final UserClient userClient;


    private final PaymentClient paymentClient;

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

    public List<ParkingSpot> getAllSpots() {
        return spotRepo.findAll();
    }

    public List<ParkingReservation> getAllReservations() {
        return reservationRepo.findAll();
    }

    public List<ParkingSpot> getSpotsByType(String type) {
        return spotRepo.findByType(type);
    }

    public List<ParkingSpot> getSpotsByTypeAndZone(String type, String zone) {
        return spotRepo.findByTypeAndZone(type, zone);
    }

    public List<ParkingSpot> getSpotsByOwner(Long ownerId) {
        return spotRepo.findByOwnerId(ownerId);
    }

    public ParkingSpot updateSpot(Long spotId, ParkingSpotDto dto) {
        ParkingSpot spot = spotRepo.findById(spotId)
                .orElseThrow(() -> new RuntimeException("Spot not found"));
        
        spot.setLocation(dto.getLocation());
        spot.setZone(dto.getZone());
        spot.setType(dto.getType());
        spot.setAvailable(dto.isAvailable());
        
        return spotRepo.save(spot);
    }

    public void deleteSpot(Long spotId) {
        spotRepo.deleteById(spotId);
    }

    public ParkingReservation reserveSpotAndPay(ReservationRequestDto dto) {
        Long userId = dto.getUserId();
        Long parkingSpotId = dto.getParkingSpotId();

        // Step 1: Check if user exists
        try {
            boolean isFound = userClient.isFound(userId);
            if (!isFound) throw new RuntimeException("User not found");
            System.out.println("User found: " + userId);
        } catch (Exception e) {
            throw new RuntimeException("User not found.");
        }

        // Step 2: Create temporary reservation
        ParkingReservation reservation = new ParkingReservation();
        reservation.setUserId(userId);
        reservation.setParkingSpotId(parkingSpotId);
        reservation.setStatus("PENDING");

        reservation = reservationRepo.save(reservation); // Get ID

        // Step 3: Process payment
        PaymentRequestDto paymentDto = new PaymentRequestDto();
        paymentDto.setUserId(userId);
        paymentDto.setReservationId(reservation.getId());
        paymentDto.setAmount(dto.getAmount());
        paymentDto.setMethod(dto.getPaymentMethod());

        try {
            Payment payment = paymentClient.makePayment(paymentDto);
            System.out.println("Payment ID: " + payment.getId());
        } catch (Exception e) {
            reservationRepo.deleteById(reservation.getId());
            throw new RuntimeException("Payment failed. Reservation cancelled.");
        }

        // Step 4: Confirm reservation
        reservation.setStatus("CONFIRMED");
        return reservationRepo.save(reservation);
    }

}