package com.marcos.payment.producer;

import com.marcos.payment.model.PaymentEvent;
import org.springframework.kafka.annotation.KafkaListener;

public class PaymentConsumer {
    @KafkaListener(topics = "payments", groupId = "fraud-group")
    public void consume(PaymentEvent event) {
        // lógica simples de fraude
    }
}
