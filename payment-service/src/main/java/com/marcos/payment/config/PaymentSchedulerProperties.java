package com.marcos.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "scheduler.payment")
public class PaymentSchedulerProperties {
    private int batchSize;
    private int maxRetry;
    private Duration retryDelay;
    private Duration delay;
}