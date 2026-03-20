package com.marcos.payment.controller;

import com.marcos.payment.dto.PaymentRequest;
import com.marcos.payment.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody PaymentRequest request) {
        service.process(request);
        return ResponseEntity.ok("Payment event sent");
    }
}