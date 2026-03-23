package com.marcos.payment.service;

import com.marcos.common.domain.Payment;
import com.marcos.common.event.PaymentEvent;
import com.marcos.common.mapper.PaymentEventMapper;
import com.marcos.payment.entities.OutboxEvent;
import com.marcos.payment.entities.OutboxEventStatus;
import com.marcos.payment.entities.PaymentEntity;
import com.marcos.payment.mapper.PaymentEntityMapper;
import com.marcos.payment.messaging.AvroDeserializer;
import com.marcos.payment.repository.OutboxEventRepository;
import com.marcos.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OutboxEventRepository outboxRepository;

    @Transactional
    public void createPayment(Payment payment) {
        PaymentEntity entity = PaymentEntityMapper.toEntity(payment);
        paymentRepository.save(entity);

        PaymentEvent event = PaymentEventMapper.toEvent(payment);
        byte[] payload = AvroDeserializer.serialize(event);

        outboxRepository.save(
                OutboxEvent.builder()
                        .id(UUID.randomUUID())
                        .aggregateId(payment.getPaymentId())
                        .eventType("PaymentCreated")
                        .payload(payload)
                        .status(OutboxEventStatus.PENDING)
                        .createdAt(Instant.now())
                        .build()
        );
    }
}