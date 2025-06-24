package com.shashi.vehicleservice.services;

import com.shashi.vehicleservice.dto.VehicleDto;
import com.shashi.vehicleservice.model.Vehicle;
import com.shashi.vehicleservice.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public Vehicle register(VehicleDto dto) {
        Vehicle vehicle = Vehicle.builder()
                .licensePlate(dto.getLicensePlate())
                .model(dto.getModel())
                .color(dto.getColor())
                .userId(dto.getUserId())
                .build();
        return vehicleRepository.save(vehicle);
    }

    public Vehicle update(Long id, VehicleDto dto) {
        Vehicle existing = getById(id);
        existing.setLicensePlate(dto.getLicensePlate());
        existing.setModel(dto.getModel());
        existing.setColor(dto.getColor());
        return vehicleRepository.save(existing);
    }

    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }

    public List<Vehicle> getByUserId(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }

    public void delete(Long id) {
        vehicleRepository.deleteById(id);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Long getVehicleCount() {
        return vehicleRepository.count();
    }
}