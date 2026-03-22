package com.marcos.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentEventPayload {
    private String eventId;
    private String userId;
    private BigDecimal amount;
    private String status;
    private long timestamp;
}
