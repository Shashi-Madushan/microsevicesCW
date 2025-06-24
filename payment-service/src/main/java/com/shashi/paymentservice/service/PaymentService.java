package com.shashi.paymentservice.service;



import com.shashi.paymentservice.config.DummyPaymentValidator;
import com.shashi.paymentservice.dto.PaymentRequestDto;
import com.shashi.paymentservice.model.Payment;
import com.shashi.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }

    public Map<String, Object> getPaymentStats() {
        List<Payment> allPayments = paymentRepository.findAll();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfMonth = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        
        Map<String, Object> stats = new HashMap<>();
        // Overall stats
        stats.put("totalPayments", allPayments.size());
        stats.put("totalSuccessful", allPayments.stream()
                .filter(p -> "SUCCESS".equals(p.getStatus())).count());
        stats.put("totalAmount", allPayments.stream()
                .mapToDouble(Payment::getAmount).sum());
        
        // Monthly stats
        List<Payment> monthlyPayments = allPayments.stream()
                .filter(p -> p.getPaidAt().isAfter(startOfMonth))
                .toList();
        
        stats.put("monthlyPayments", monthlyPayments.size());
        stats.put("monthlySuccessful", monthlyPayments.stream()
                .filter(p -> "SUCCESS".equals(p.getStatus())).count());
        stats.put("monthlyAmount", monthlyPayments.stream()
                .mapToDouble(Payment::getAmount).sum());
        stats.put("month", now.getMonth().toString());
        
        return stats;
    }
}