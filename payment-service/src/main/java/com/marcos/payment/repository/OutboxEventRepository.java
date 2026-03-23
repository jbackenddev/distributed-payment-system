package com.marcos.payment.repository;

import com.marcos.payment.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query(value = """
        SELECT * FROM outbox_event
        WHERE status = 'PENDING'
        AND (next_retry_at IS NULL OR next_retry_at <= now())
        ORDER BY created_at
        LIMIT :limit
        FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<OutboxEvent> findBatchForUpdate(@Param("limit") int limit);
}
