package com.shashi.vehicleservice.controller;

import com.shashi.vehicleservice.dto.VehicleDto;
import com.shashi.vehicleservice.model.Vehicle;
import com.shashi.vehicleservice.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<Vehicle> register(@RequestBody VehicleDto dto) {
        return ResponseEntity.ok(vehicleService.register(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> update(@PathVariable Long id, @RequestBody VehicleDto dto) {
        return ResponseEntity.ok(vehicleService.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Vehicle>> getByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(vehicleService.getByUserId(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}