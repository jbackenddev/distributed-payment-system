package com.marcos.payment.service;

import com.marcos.common.model.PaymentEvent;
import com.marcos.payment.dto.PaymentRequest;
import com.marcos.payment.producer.PaymentProducer;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentProducer producer;

    public PaymentService(PaymentProducer producer) {
        this.producer = producer;
    }

    public void process(PaymentRequest request) {
        PaymentEvent event = PaymentEvent.build(request.getUserId(),
                request.getAmount());

        producer.send(event);
    }
}