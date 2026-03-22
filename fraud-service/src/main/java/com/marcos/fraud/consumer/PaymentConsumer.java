package com.marcos.fraud.consumer;

import com.marcos.common.domain.Payment;
import com.marcos.common.event.PaymentEvent;
import com.marcos.common.exceptions.EventAlreadyProcessedException;
import com.marcos.common.mapper.PaymentEventMapper;
import com.marcos.fraud.service.ProcessPaymentService;
import com.marcos.fraud.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentConsumer {
    private final IdempotencyService idempotencyService;
    private final ProcessPaymentService processPaymentService;

    @KafkaListener(
            topics = "payments",
            groupId = "fraud-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(PaymentEvent event) {
        String eventId = event.getEventId().toString();

        try {
            idempotencyService.checkAndMarkAsProcessed(eventId);
            Payment payment = PaymentEventMapper.fromEvent(event);
            processPaymentService.processPayment(payment);
        } catch (EventAlreadyProcessedException ex) {
            System.out.println("event already processed: " + eventId);
        }
    }

    @KafkaListener(topics = "payments-DLT")
    public void consumeDlq(PaymentEvent event) {
        System.out.println("DLQ EVENT: " + event);
    }

}