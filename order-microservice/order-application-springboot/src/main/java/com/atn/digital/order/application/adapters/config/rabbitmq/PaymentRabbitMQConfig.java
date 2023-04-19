package com.atn.digital.order.application.adapters.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.payment")
public record PaymentRabbitMQConfig(String exchangeName, String exchangeType,
                                    String paymentCompletedQueueName, String paymentCompletedRoutingKey,
                                    String paymentFailedQueueName, String paymentFailedRoutingKey) { }
