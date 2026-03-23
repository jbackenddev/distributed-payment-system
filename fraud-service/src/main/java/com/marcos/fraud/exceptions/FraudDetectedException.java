package com.marcos.fraud.exceptions;

public class FraudDetectedException extends RuntimeException {

    public FraudDetectedException(String paymentId) {
        super("Payment is a fraude: " + paymentId);
    }
}