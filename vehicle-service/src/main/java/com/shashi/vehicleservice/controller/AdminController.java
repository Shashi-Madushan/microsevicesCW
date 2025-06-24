package com.shashi.vehicleservice.controller;

import com.shashi.vehicleservice.model.Vehicle;
import com.shashi.vehicleservice.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/vehicles")
@RequiredArgsConstructor
public class AdminController {

    private final VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.getAllVehicles());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getVehicleCount() {
        return ResponseEntity.ok(vehicleService.getVehicleCount());
    }
}
