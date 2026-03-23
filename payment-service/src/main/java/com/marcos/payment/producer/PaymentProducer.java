package com.marcos.payment.producer;

import com.marcos.common.event.PaymentEvent;
import com.marcos.payment.config.PaymentSchedulerProperties;
import com.marcos.payment.entities.OutboxEvent;
import com.marcos.payment.entities.OutboxEventStatus;
import com.marcos.payment.messaging.AvroDeserializer;
import com.marcos.payment.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentProducer {

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    private final PaymentSchedulerProperties properties;

    @Scheduled(fixedDelayString = "#{@paymentSchedulerProperties.delay.toMillis()}")
    @Transactional
    public void publish() {
        List<OutboxEvent> events = repository.findReadyToPublish(properties.getBatchSize(), Instant.now());

        for (OutboxEvent event : events) {
            process(event);
        }
    }

    private void process(OutboxEvent event) {
        try {
            log.info("Publishing event eventId={} retry={}", event.getId(), event.getRetryCount());

            PaymentEvent payload = AvroDeserializer.deserialize( event.getPayload(), new PaymentEvent());

            sendWithHeaders(payload);

            event.changeStatus(OutboxEventStatus.SENT);
            repository.save(event);

            log.info("Event sent successfully eventId={}", event.getId());
        } catch (Exception e) {
            log.error("Event processing failed eventId={}", event.getId(), e);
            handleFailure(event, e);
        }
    }

    private void sendWithHeaders(PaymentEvent payload) {
        String correlationId = MDC.get("correlationId");

        ProducerRecord<String, PaymentEvent> recordEvent = new ProducerRecord<>("payments", payload);

        if (correlationId != null) {
            recordEvent.headers().add(new RecordHeader( "X-Correlation-Id", correlationId.getBytes()));
        }

        //kafkaTemplate.send(recordEvent);
        kafkaTemplate.send("payments", payload);
    }

    private void handleFailure(OutboxEvent event, Exception e) {
        log.info("Handling failure during event processing eventId={} retry={} error={}",
                event.getId(), event.getRetryCount(), e.getMessage());

        event.incrementRetryCount();

        if (event.getRetryCount() >= properties.getMaxRetry()) {
            event.changeStatus(OutboxEventStatus.FAILED);
            log.info("Retry event processing Failed after max ({}) attempts", event.getRetryCount());
        } else {
            log.info("Retrying event processing, new attempt: {}", event.getRetryCount());
            event.changeStatus(OutboxEventStatus.RETRY);
            Duration delay = properties.getRetryDelay();

            event.changeNextRetryAt(
                    Instant.now().plus(delay.multipliedBy(event.getRetryCount()))
            );
        }

        repository.save(event);
    }
}