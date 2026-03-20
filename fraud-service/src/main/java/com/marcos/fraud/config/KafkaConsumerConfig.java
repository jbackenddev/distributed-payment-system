package com.marcos.fraud.config;

import com.marcos.common.model.PaymentEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class KafkaConsumerConfig {

    @Bean
    public JsonDeserializer<PaymentEvent> jsonDeserializer() {
        JsonDeserializer<PaymentEvent> deserializer =
                new JsonDeserializer<>(PaymentEvent.class);

        deserializer.addTrustedPackages("com.marcos.common");

        return deserializer;
    }
}