package com.marcos.common.helper;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public class KafkaHeaderUtils {

    private KafkaHeaderUtils() {}

    public static String getHeader(ConsumerRecord<?, ?> recordEvent, String key) {
        var header = recordEvent.headers().lastHeader(key);
        return header != null ? new String(header.value()) : null;
    }
}