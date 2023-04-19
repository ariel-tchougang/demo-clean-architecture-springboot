package com.atn.digital.payment.application.adapters.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.host")
public record RabbitMQHostConfig(String uri, String username, String password) { }
