package com.atn.digital.order.application.adapters.config.dynamodb;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dynamodb.orders")
public record DynamoDbConfig(String uri, String tableName) { }
