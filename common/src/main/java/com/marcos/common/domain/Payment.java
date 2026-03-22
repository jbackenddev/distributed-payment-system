package com.marcos.common.domain;

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
public class Payment {

    private String paymentId;
    private String userId;
    private String orderId;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;

    public static Payment build(String userId, String orderId, String currency, BigDecimal amount) {
        validate(amount);

        return Payment.builder()
                .paymentId(UUID.randomUUID().toString())
                .userId(userId)
                .orderId(orderId)
                .currency(currency)
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