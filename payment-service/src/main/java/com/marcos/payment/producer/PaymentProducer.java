package com.marcos.payment.producer;

import com.marcos.common.event.PaymentEvent;
import com.marcos.payment.entities.OutboxEvent;
import com.marcos.payment.entities.OutboxEventStatus;
import com.marcos.payment.messaging.AvroDeserializer;
import com.marcos.payment.repository.OutboxEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentProducer {
    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    private static final int BATCH_SIZE = 10;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publish() {
        List<OutboxEvent> events = repository.findBatchForUpdate(BATCH_SIZE);

        for (OutboxEvent event : events) {
            process(event);
            repository.save(event);
        }
    }

    private void process(OutboxEvent event) {
        try {
            PaymentEvent payload = AvroDeserializer.deserialize(event.getPayload(), new PaymentEvent());

            kafkaTemplate.send("payments", payload)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            event.changeStatus(OutboxEventStatus.SENT);
                        } else {
                            handleFailure(event, (Exception) ex);
                        }
                    });

            event.changeStatus(OutboxEventStatus.SENT);
        } catch (Exception e) {
            handleFailure(event, e);
        }
    }

    private void handleFailure(OutboxEvent event, Exception e) {
        if (event.getRetryCount() >= 3) {
            event.changeStatus(OutboxEventStatus.FAILED);
            return;
        }

        event.incrementRetryCount();
        event.changeNextRetryAt(Instant.now().plusSeconds(10L * event.getRetryCount()));
    }
}