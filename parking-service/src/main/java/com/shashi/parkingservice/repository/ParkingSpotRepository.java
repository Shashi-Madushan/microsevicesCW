package com.shashi.parkingservice.repository;

import com.shashi.parkingservice.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findByAvailableTrue();
    List<ParkingSpot> findByZone(String zone);
    List<ParkingSpot> findByOwnerId(Long ownerId);
    List<ParkingSpot> findByTypeAndZone(String type, String zone);
    List<ParkingSpot> findByType(String type);
}