package com.shashi.paymentservice.service;



import com.shashi.paymentservice.config.DummyPaymentValidator;
import com.shashi.paymentservice.dto.PaymentRequestDto;
import com.shashi.paymentservice.model.Payment;
import com.shashi.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final DummyPaymentValidator validator;

    public Payment processPayment(PaymentRequestDto dto) {
        String status = "SUCCESS";

        if ("CARD".equalsIgnoreCase(dto.getMethod())) {
            if (!validator.isValidCard(dto.getCardNumber())) {
                status = "FAILED";
            }
        }

        Payment payment = Payment.builder()
                .userId(dto.getUserId())
                .reservationId(dto.getReservationId())
                .amount(dto.getAmount())
                .method(dto.getMethod())
                .status(status)
                .paidAt(LocalDateTime.now())
                .transactionId(UUID.randomUUID().toString())
                .build();

        return paymentRepository.save(payment);
    }

    public Payment getById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

    public List<Payment> getByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }

    public Optional<Payment> getByReservationId(Long reservationId) {
        return paymentRepository.findByReservationId(reservationId);
    }
}
