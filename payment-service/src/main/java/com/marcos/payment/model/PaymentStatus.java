package com.marcos.payment.model;

public enum PaymentStatus {

    PENDING,
    APPROVED,
    REJECTED;

    public boolean isFinal() {
        return this == APPROVED || this == REJECTED;
    }
}