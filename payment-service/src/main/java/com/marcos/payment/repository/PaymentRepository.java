package com.marcos.payment.repository;

import com.marcos.payment.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
}