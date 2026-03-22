package com.marcos.payment.exception;

public class PaymentEventSerializationException extends RuntimeException {

    public PaymentEventSerializationException(String message) {
        super(message);
    }

    public PaymentEventSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
