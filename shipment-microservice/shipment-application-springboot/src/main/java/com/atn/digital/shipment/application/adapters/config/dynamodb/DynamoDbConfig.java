package com.atn.digital.shipment.application.adapters.config.dynamodb;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dynamodb.shipments")
public record DynamoDbConfig(String uri, String tableName) {

}
