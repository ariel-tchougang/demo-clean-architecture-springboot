package com.atn.digital.shipment.adapters.out.persistence.dynamodb;

import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableRequest;
import software.amazon.awssdk.services.dynamodb.model.DescribeTableResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.ArrayList;
import java.util.List;

public class DynamoDbTableCreator {

    public static DynamoDbClient initialize(String dynamoDbLocalUri) {
        // Set your AWS credentials as environment variables or in the ~/.aws/credentials file

        DynamoDbClient client = DynamoDbClientFactory.createLocalDbClient(dynamoDbLocalUri);
        String tableName = "Shipments";

        if (!tableExists(client, tableName)) {
            createTable(client, tableName);
        }

        return client;
    }

    private static boolean tableExists(DynamoDbClient client, String tableName) {
        return client.listTables().tableNames().contains(tableName);
    }

    private static void createTable(DynamoDbClient client, String tableName) {

        List<KeySchemaElement> keySchema = new ArrayList<> ();
        keySchema.add(KeySchemaElement.builder().attributeName("id").keyType(KeyType.HASH.name()).build());

        List<AttributeDefinition> attributes = new ArrayList<>();
        attributes.add(AttributeDefinition.builder().attributeName("id")
                .attributeType(ScalarAttributeType.S.name()).build());

        ProvisionedThroughput provisionedThroughput = ProvisionedThroughput.builder()
                .readCapacityUnits(5L)
                .writeCapacityUnits(5L)
                .build();

        CreateTableRequest request = CreateTableRequest.builder()
                .tableName(tableName)
                .keySchema(keySchema)
                .attributeDefinitions(attributes)
                .provisionedThroughput(provisionedThroughput)
                .build();
        try {
            client.createTable(request);
            System.out.println("Table " + tableName + " created successfully.");

            DescribeTableRequest tableRequest = DescribeTableRequest.builder()
                    .tableName(tableName)
                    .build();

            DynamoDbWaiter dbWaiter = client.waiter();
            WaiterResponse<DescribeTableResponse> waiterResponse = dbWaiter.waitUntilTableExists(tableRequest);
            waiterResponse.matched().response().ifPresent(System.out::println);

        } catch (DynamoDbException e) {
            System.err.println("Failed to create table " + tableName);
            e.printStackTrace();
        }
    }
}

