package com.marcos.payment.controller;

import com.marcos.common.domain.Payment;
import com.marcos.payment.dto.PaymentRequest;
import com.marcos.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;


    @PostMapping
    public ResponseEntity<String> create(@RequestBody PaymentRequest request) {
        Payment payment = Payment.build(
                request.getUserId(),
                request.getOrderId(),
                request.getCurrency(),
                request.getAmount()
        );

        paymentService.createPayment(payment);

        return ResponseEntity.ok("Payment event created: " + payment.getPaymentId());
    }
}