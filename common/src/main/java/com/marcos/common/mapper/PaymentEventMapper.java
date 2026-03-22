package com.marcos.common.mapper;

import com.marcos.common.avro.AvroDecimalUtils;
import com.marcos.common.domain.Payment;
import com.marcos.common.domain.PaymentStatus;
import com.marcos.common.event.PaymentEvent;

import java.time.Instant;
import java.util.UUID;

public class PaymentEventMapper {

    private PaymentEventMapper() {}

    public static PaymentEvent toEvent(Payment payment) {
        if (payment == null) {
            return null;
        }

        return PaymentEvent.newBuilder()
                .setEventId(
                        payment.getPaymentId() != null
                                ? payment.getPaymentId()
                                : UUID.randomUUID().toString()
                )
                .setUserId(payment.getUserId())
                .setOrderId(payment.getOrderId())
                .setAmount(AvroDecimalUtils.convertToBytes(payment.getAmount()))
                .setCurrency(payment.getCurrency())
                .setStatus(payment.getStatus().name())
                .setTimestamp(Instant.now().toEpochMilli())
                .build();
    }

    public static Payment fromEvent(PaymentEvent event) {
        if (event == null) {
            return null;
        }

        return Payment.builder()
                .paymentId(event.getEventId().toString())
                .userId(event.getUserId().toString())
                .amount(AvroDecimalUtils.convertToBigDecimal(event.getAmount()))
                .status(parseStatus(event.getStatus().toString()))
                .build();
    }

    private static PaymentStatus parseStatus(String status) {
        try {
            return PaymentStatus.valueOf(status);
        } catch (Exception e) {
            return PaymentStatus.PENDING;
        }
    }
}