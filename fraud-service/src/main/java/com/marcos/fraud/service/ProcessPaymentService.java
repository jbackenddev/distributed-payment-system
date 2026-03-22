package com.marcos.fraud.service;

import com.marcos.common.domain.Payment;
import org.springframework.stereotype.Service;

@Service
public class ProcessPaymentService {
    public void processPayment(Payment payment) {
        System.out.println("Received payment: " + payment.getPaymentId());
        if (payment.getAmount() == null || payment.getAmount().doubleValue() == 0.0) {
            throw new IllegalArgumentException("Invalid event, invalid ammount");
        }


        if (payment.getAmount().doubleValue() > 1000) {
            System.out.println("Fraud detected!");
        } else {
            System.out.println("Payment approved");
        }
    }
}
