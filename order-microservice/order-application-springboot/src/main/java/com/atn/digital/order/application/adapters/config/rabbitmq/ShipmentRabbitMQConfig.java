package com.atn.digital.order.application.adapters.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.shipment")
public record ShipmentRabbitMQConfig(String exchangeName, String exchangeType,
                                     String shipmentCreatedQueueName, String shipmentCreatedRoutingKey,
                                     String shipmentStatusChangedQueueName, String shipmentStatusChangedRoutingKey) { }
