package com.atn.digital.order.adapters.out.persistence.dynamodb;

import com.atn.digital.order.adapters.out.persistence.OrderRepositoryAdapter;
import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.Optional;
import java.util.UUID;

public class DynamoDbOrderRepository extends OrderRepositoryAdapter {

    private final DynamoDbClient client;

    private final OrderMapper mapper = new OrderMapper();

    public DynamoDbOrderRepository(DynamoDbClient client) {
        this.client = client;
    }

    public OrderId createNewOrder(Order newOrder) {
        DynamoDbTable<OrderEntity> table = getOrdersDynamoDbTable();
        OrderEntity dbEntity = mapper.toOrderEntity(newOrder);
        dbEntity.setId(UUID.randomUUID().toString());
        table.putItem(dbEntity);
        return new OrderId(dbEntity.getId());
    }

    public Optional<Order> loadOrder(OrderId orderId) {
        DynamoDbTable<OrderEntity> table = getOrdersDynamoDbTable();
        OrderEntity dbEntity = table.getItem(Key.builder().partitionValue(orderId.getId()).build());
        return dbEntity == null ? Optional.empty() : Optional.of(mapper.toOrder(dbEntity));
    }

    public Order update(Order order) {
        DynamoDbTable<OrderEntity> table = getOrdersDynamoDbTable();
        OrderEntity dbEntity = mapper.toOrderEntity(order);
        table.putItem(dbEntity);
        dbEntity = table.getItem(Key.builder().partitionValue(dbEntity.getId()).build());
        return mapper.toOrder(dbEntity);
    }

    private DynamoDbTable<OrderEntity> getOrdersDynamoDbTable() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build()
                .table(System.getProperty("ORDER_TABLE"), TableSchema.fromBean(OrderEntity.class));
    }
}
