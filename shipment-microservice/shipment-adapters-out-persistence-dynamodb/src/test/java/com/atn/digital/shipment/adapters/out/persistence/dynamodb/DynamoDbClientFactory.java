package com.atn.digital.shipment.adapters.out.persistence.dynamodb;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public final class DynamoDbClientFactory {

    private DynamoDbClientFactory() {
        // NOP
    }

    public static DynamoDbClient createLocalDbClient(String dynamoDbLocalUri) {
        return DynamoDbClient.builder().endpointOverride(URI.create(dynamoDbLocalUri)).build();
    }
}
