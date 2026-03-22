package com.marcos.common.domain;

public enum PaymentStatus {

    PENDING,
    APPROVED,
    REJECTED;

    public boolean isFinal() {
        return this == APPROVED || this == REJECTED;
    }
}