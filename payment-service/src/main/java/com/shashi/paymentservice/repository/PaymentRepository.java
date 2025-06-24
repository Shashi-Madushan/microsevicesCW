package com.shashi.paymentservice.repository;




import com.shashi.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByUserId(Long userId);
    Optional<Payment> findByReservationId(Long reservationId);
    List<Payment> findByStatus(String status);
}