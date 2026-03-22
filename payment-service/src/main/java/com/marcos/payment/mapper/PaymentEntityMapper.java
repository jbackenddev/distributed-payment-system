package com.marcos.payment.mapper;

import com.marcos.common.domain.Payment;
import com.marcos.payment.entities.PaymentEntity;

import java.time.Instant;

public class PaymentEntityMapper {

    private PaymentEntityMapper() {}

    public static PaymentEntity toEntity(Payment payment) {
        return PaymentEntity.builder()
                .id(payment.getPaymentId())
                .userId(payment.getUserId())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .createdAt(Instant.now())
                .build();
    }

    public static Payment toDomain(PaymentEntity entity) {
        return Payment.builder()
                .paymentId(entity.getId())
                .userId(entity.getUserId())
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .build();
    }
}