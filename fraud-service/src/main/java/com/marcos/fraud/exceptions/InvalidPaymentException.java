package com.marcos.fraud.exceptions;

public class InvalidPaymentException extends RuntimeException {

    public InvalidPaymentException(String paymentId, String message) {
        super("Payment is invalid: " + paymentId + " - " + message);
    }
}