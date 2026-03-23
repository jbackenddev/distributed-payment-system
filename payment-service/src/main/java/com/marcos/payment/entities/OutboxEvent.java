package com.marcos.payment.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_event")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OutboxEvent {

    @Id
    private UUID id;
    private String aggregateId;
    private String eventType;
    @Lob
    @Column(nullable = false)
    private byte[] payload;
    @Enumerated(EnumType.STRING)
    private OutboxEventStatus status;
    private Instant createdAt;
    private int retryCount;
    private Instant nextRetryAt;

    public void changeStatus(OutboxEventStatus status) {
        this.status = status;
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

    public void changeNextRetryAt(Instant nextRetryAt) {
        this.nextRetryAt = nextRetryAt;
    }
}