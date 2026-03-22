package com.marcos.fraud.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "processed_events")
@Getter
@NoArgsConstructor
public class ProcessedEvent {
    @Id
    private String eventId;
    private Instant processedAt;

    public ProcessedEvent(String eventId, Instant processedAt) {
        this.eventId = eventId;
        this.processedAt = processedAt;
    }
}