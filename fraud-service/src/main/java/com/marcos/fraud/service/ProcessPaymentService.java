package com.marcos.fraud.service;

import com.marcos.common.domain.Payment;
import com.marcos.common.domain.PaymentStatus;
import com.marcos.common.event.FraudResultEvent;
import com.marcos.fraud.exceptions.FraudDetectedException;
import com.marcos.fraud.exceptions.InvalidPaymentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPaymentService {
    private final KafkaTemplate<String, FraudResultEvent> kafkaTemplate;

    public void processPayment(Payment payment) {
        log.info("Received payment to process: {}", payment.getPaymentId());

        try {
            validatePayment(payment);
            analyzePayment(payment);
            publishResult(payment, PaymentStatus.APPROVED, null);
            log.info("Payment processed successfully: {}, PaymentStatus: {}",
                    payment.getPaymentId(), PaymentStatus.APPROVED);
        } catch (FraudDetectedException ex) {
            publishResult(payment, PaymentStatus.REJECTED, ex.getMessage());
            log.info("Payment processed fraud suspect: {}, PaymentStatus: {}",
                    payment.getPaymentId(), PaymentStatus.REJECTED);
        } catch (Exception ex) {
            log.error("Payment processed through exception {}", ex.getMessage());
            throw ex;
        }
    }

    private void publishResult(Payment payment, PaymentStatus status, String reason) {
        try {
            FraudResultEvent result = FraudResultEvent.newBuilder()
                    .setEventId(UUID.randomUUID().toString())
                    .setPaymentId(payment.getPaymentId())
                    .setStatus(status.toString())
                    .setReason(reason)
                    .setTimestamp(System.currentTimeMillis())
                    .build();

            kafkaTemplate.send("fraud-result", result);
            log.info("Payment result published {}", result);
        } catch (Exception ex) {
            log.error("Error trying to publish the Payment result {}", ex.getMessage());
            throw ex;
        }
    }

    private void validatePayment(Payment payment) {
        if (payment.getAmount() == null || payment.getAmount().doubleValue() == 0.0) {
            throw new InvalidPaymentException(payment.getPaymentId(), "Invalid event, invalid ammount");
        }
    }

    private void analyzePayment(Payment payment) {
        if (payment.getAmount().doubleValue() > 1000) {
            throw new FraudDetectedException(payment.getPaymentId());
        }
    }
}
