package com.shashi.paymentservice.controller;

import com.shashi.paymentservice.dto.PaymentRequestDto;
import com.shashi.paymentservice.model.Payment;
import com.shashi.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<Payment> pay(@RequestBody PaymentRequestDto dto) {
        return ResponseEntity.ok(paymentService.processPayment(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getByUserId(userId));
    }

    @GetMapping("/reservation/{reservationId}")
    public ResponseEntity<Payment> getByReservation(@PathVariable Long reservationId) {
        return paymentService.getByReservationId(reservationId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
