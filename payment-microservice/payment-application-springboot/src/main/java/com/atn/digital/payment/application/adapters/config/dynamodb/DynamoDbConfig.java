package com.atn.digital.payment.application.adapters.config.dynamodb;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dynamodb.payments")
public record DynamoDbConfig(String uri, String tableName) { }
