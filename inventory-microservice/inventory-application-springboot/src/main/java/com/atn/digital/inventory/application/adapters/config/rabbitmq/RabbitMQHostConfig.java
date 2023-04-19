package com.atn.digital.inventory.application.adapters.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.host")
public record RabbitMQHostConfig(String uri, String username, String password) { }
