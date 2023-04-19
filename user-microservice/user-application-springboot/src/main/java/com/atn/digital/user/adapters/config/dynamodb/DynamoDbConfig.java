package com.atn.digital.user.adapters.config.dynamodb;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dynamodb.users")
public record DynamoDbConfig(String uri, String tableName) { }
