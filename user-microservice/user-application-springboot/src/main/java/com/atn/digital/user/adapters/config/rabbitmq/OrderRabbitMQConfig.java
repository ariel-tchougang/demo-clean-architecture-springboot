package com.atn.digital.user.adapters.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.order")
public record OrderRabbitMQConfig(String exchangeName, String exchangeType,
                                  String orderCreatedQueueName, String orderCreatedRoutingKey,
                                  String orderStatusChangedQueueName, String orderStatusChangedRoutingKey) { }
