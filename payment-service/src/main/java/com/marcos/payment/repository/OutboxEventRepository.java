package com.marcos.payment.repository;

import com.marcos.payment.entities.OutboxEvent;
import com.marcos.payment.entities.OutboxEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    List<OutboxEvent> findTop10ByStatusOrderByCreatedAtAsc(OutboxEventStatus status);
}
