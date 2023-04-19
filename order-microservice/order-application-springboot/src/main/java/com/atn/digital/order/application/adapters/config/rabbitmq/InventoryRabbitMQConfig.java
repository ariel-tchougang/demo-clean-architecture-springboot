package com.atn.digital.order.application.adapters.config.rabbitmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rabbitmq.inventory")
public record InventoryRabbitMQConfig(String exchangeName, String exchangeType,
                                      String sufficientStockQueueName, String sufficientStockRoutingKey,
                                      String insufficientStockQueueName, String insufficientStockRoutingKey) { }
