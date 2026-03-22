package com.marcos.payment.producer;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentProducer {
    private final OutboxEventRepository repository;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publish() {
        List<OutboxEvent> events = repository.findTop10ByStatusOrderByCreatedAtAsc(OutboxEventStatus.PENDING);

        for (OutboxEvent event : events) {
            try {
                PaymentEvent payload = AvroDeserializer.deserialize(event.getPayload(), new PaymentEvent());

                kafkaTemplate.send("payments", payload)
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                event.changeStatus(OutboxEventStatus.SENT);
                                repository.save(event);
                            }
                        });
            } catch (Exception e) {
                // add logger
            }
        }
    }
}