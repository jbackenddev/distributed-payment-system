package com.marcos.payment.entities;

public enum OutboxEventStatus {
    PENDING,
    FAILED,
    RETRY,
    SENT;

}