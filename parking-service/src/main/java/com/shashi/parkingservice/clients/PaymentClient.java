package com.shashi.parkingservice.clients;

import com.shashi.parkingservice.model.Payment;
import com.shashi.parkingservice.model.PaymentRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment-service")
public interface PaymentClient {
    @PostMapping("/payments/pay")
    Payment makePayment(@RequestBody PaymentRequestDto dto);
}
