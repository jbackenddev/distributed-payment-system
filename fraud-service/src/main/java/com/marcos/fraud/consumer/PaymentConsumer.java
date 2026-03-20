package com.marcos.fraud.consumer;

import com.marcos.common.model.PaymentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentConsumer {

    @KafkaListener(
            topics = "payments",
            groupId = "fraud-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consume(PaymentEvent event) {

        System.out.println("Received payment: " + event.getPaymentId());

        if (event.getAmount().doubleValue() > 1000) {
            System.out.println("Fraud detected!");
        } else {
            System.out.println("Payment approved");
        }
    }
}