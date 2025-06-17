package com.shashi.parkingservice.repository;

import com.shashi.parkingservice.model.ParkingReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParkingReservationRepository extends JpaRepository<ParkingReservation, Long> {
    List<ParkingReservation> findByUserId(Long userId);
}