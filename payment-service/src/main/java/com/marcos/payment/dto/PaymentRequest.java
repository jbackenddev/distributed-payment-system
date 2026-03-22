package com.marcos.payment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
public class PaymentRequest {

    @NotNull(message = "UserId is required")
    private String userId;
    @NotNull(message = "OrderId is required")
    private String orderId;
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than zero")
    private BigDecimal amount;
    @NotNull(message = "Currency is required")
    private String currency;
}