package com.marcos.payment.service;

import com.marcos.common.avro.AvroDecimalUtils;
import com.marcos.common.domain.PaymentStatus;
import com.marcos.common.event.PaymentEvent;
import com.marcos.payment.dto.PaymentRequest;
import com.marcos.payment.producer.PaymentProducer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentProducer producer;

    public PaymentService(PaymentProducer producer) {
        this.producer = producer;
    }

    public void process(PaymentRequest request) {
        PaymentEvent event = PaymentEvent.newBuilder()
                .setEventId(request.getUserId() + request.getOrderId())
                .setUserId(request.getUserId())
                .setOrderId(request.getOrderId())
                .setAmount(AvroDecimalUtils.convertToBytes(request.getAmount()))
                .setCurrency(request.getCurrency())
                .setStatus(PaymentStatus.PENDING.toString())
                .setTimestamp(System.currentTimeMillis())
                .build();

        producer.send(event);
    }
}