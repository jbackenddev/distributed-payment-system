package com.marcos.fraud.consumer;

import com.marcos.common.domain.Payment;
import com.marcos.common.event.PaymentEvent;
import com.marcos.common.helper.KafkaHeaderUtils;
import com.marcos.fraud.exceptions.EventAlreadyProcessedException;
import com.marcos.common.mapper.PaymentEventMapper;
import com.marcos.fraud.service.IdempotencyService;
import com.marcos.fraud.service.ProcessPaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
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
    public void consume(ConsumerRecord<String, PaymentEvent> recordEvent) {
        String eventId = recordEvent.value().getEventId().toString();

        try {
            String correlationId = KafkaHeaderUtils.getHeader(recordEvent, "X-Correlation-Id");

            if (correlationId != null) {
                MDC.put("correlationId", correlationId);
            }

            log.info("Processing payment paymentId={}", recordEvent.value().getEventId());

            idempotencyService.checkAndMarkAsProcessed(eventId);
            Payment payment = PaymentEventMapper.fromEvent(recordEvent.value());
            processPaymentService.processPayment(payment);
        } catch (EventAlreadyProcessedException ex) {
            log.info("Event already processed: {}", eventId);
        } finally {
            MDC.clear();
        }
    }

    @KafkaListener(topics = "payments-dlq")
    public void consumeDlq(ConsumerRecord<String, PaymentEvent> recordEvent) {
        String correlationId = MDC.get("correlationId");

        recordEvent.headers().add(
                new RecordHeader("X-Correlation-Id", correlationId.getBytes())
        );

        PaymentEvent event = recordEvent.value();
        String exception = KafkaHeaderUtils.getHeader(recordEvent, "x-exception-message");
        String stacktrace = KafkaHeaderUtils.getHeader(recordEvent, "x-exception-stacktrace");

        log.info("DLQ EVENT RECEIVED - paymentId={} - exception={} - stacktrace={}",
                event.getEventId(), exception, stacktrace);
    }
}