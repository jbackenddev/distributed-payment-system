package com.marcos.payment.repository;

import com.marcos.payment.entities.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {
    @Query(value = """
        SELECT * FROM outbox_event e
        WHERE e.status IN ('PENDING', 'RETRY')
          AND (e.next_retry_at IS NULL OR e.next_retry_at <= :now)
        ORDER BY e.created_at
        LIMIT :limit
        FOR UPDATE SKIP LOCKED
    """, nativeQuery = true)
    List<OutboxEvent> findReadyToPublish(@Param("limit") int limit, Instant now);
}
