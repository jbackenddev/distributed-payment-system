package com.marcos.fraud.repository;

import com.marcos.fraud.entities.ProcessedEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, String> {
    @Modifying
    @Query(value = """
        INSERT INTO processed_events(event_id, processed_at)
        VALUES (:id, now())
        ON CONFLICT DO NOTHING
      """,
            nativeQuery = true
    )
    int insertIfNotExists(@Param("id") String id);
}