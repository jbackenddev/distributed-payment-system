package com.marcos.fraud.service;

import com.marcos.fraud.exceptions.EventAlreadyProcessedException;
import com.marcos.fraud.entities.ProcessedEvent;
import com.marcos.fraud.repository.ProcessedEventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final ProcessedEventRepository processedEventRepository;

    @Transactional
    public void checkAndMarkAsProcessed(String eventId) {
        int inserted = processedEventRepository.insertIfNotExists(eventId);

        if (inserted == 0) {
            throw new EventAlreadyProcessedException(eventId);
        }

        processedEventRepository.save(new ProcessedEvent(eventId, Instant.now()));
    }
}
