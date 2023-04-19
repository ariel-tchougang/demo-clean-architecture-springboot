package com.atn.digital.shipment.adapters.out.persistence.dynamodb;

import com.atn.digital.shipment.adapters.out.persistence.ShipmentRepositoryAdapter;
import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;
import java.util.UUID;

public class DynamoDbShipmentRepository extends ShipmentRepositoryAdapter {

    private final DynamoDbClient client;

    private final ShipmentMapper mapper = new ShipmentMapper();

    public DynamoDbShipmentRepository(DynamoDbClient client) {
        this.client = client;
    }

    public ShipmentId createNewShipment(Shipment newShipment) {
        DynamoDbTable<ShipmentEntity> table = getShipmentsDynamoDbTable();
        ShipmentEntity dbEntity = mapper.toShipmentEntity(newShipment);
        dbEntity.setId(UUID.randomUUID().toString());
        table.putItem(dbEntity);
        return new ShipmentId(dbEntity.getId());
    }

    public Optional<Shipment> loadShipment(ShipmentId orderId) {
        DynamoDbTable<ShipmentEntity> table = getShipmentsDynamoDbTable();
        ShipmentEntity dbEntity = table.getItem(Key.builder().partitionValue(orderId.getId()).build());
        return dbEntity == null ? Optional.empty() : Optional.of(mapper.toShipment(dbEntity));
    }

    public Shipment update(Shipment order) {
        DynamoDbTable<ShipmentEntity> table = getShipmentsDynamoDbTable();
        ShipmentEntity dbEntity = mapper.toShipmentEntity(order);
        table.putItem(dbEntity);
        dbEntity = table.getItem(Key.builder().partitionValue(dbEntity.getId()).build());
        return mapper.toShipment(dbEntity);
    }

    private DynamoDbTable<ShipmentEntity> getShipmentsDynamoDbTable() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build()
                .table(System.getProperty("SHIPMENT_TABLE"), TableSchema.fromBean(ShipmentEntity.class));
    }
}
