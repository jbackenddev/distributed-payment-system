package com.marcos.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEvent {

    private String paymentId;
    private String userId;
    private BigDecimal amount;
    private PaymentStatus status;

    public static PaymentEvent build(String userId, BigDecimal amount) {
        validate(amount);

        return PaymentEvent.builder()
                .paymentId(UUID.randomUUID().toString())
                .userId(userId)
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .build();
    }

    private static void validate(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
    }
}