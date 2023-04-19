package com.atn.digital.user.adapters.out.persistence.dynamodb;

import com.atn.digital.user.adapters.out.persistence.OrderRepositoryAdapter;
import com.atn.digital.user.domain.models.Order;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DynamoDbOrderRepository extends OrderRepositoryAdapter {

    private final DynamoDbClient client;

    private final DynamoDbUserRepository userRepository;

    private final OrderEntityMapper mapper = new OrderEntityMapper();

    public DynamoDbOrderRepository(DynamoDbUserRepository userRepository, DynamoDbClient client) {
        this.userRepository = userRepository;
        this.client = client;
    }

    public void addNewOrder(Order order) {
        UserEntity userEntity = userRepository.findByUserId(order.userId());

        if (userEntity.getOrders() == null) {
            userEntity.setOrders(new ArrayList<>());
        }

        userEntity.getOrders().add(mapper.toOrderEntity(order));
        DynamoDbTable<UserEntity> table = getUserEntityDynamoDbTable();
        table.putItem(userEntity);
    }

    public List<Order> findOrdersByUserId(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null || userEntity.getOrders() == null) {
            return new ArrayList<>();
        }

        return userEntity.getOrders().stream().map(mapper::toOrder).toList();
    }

    public void updateStatus(String userId, String orderId, String status, String reason) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        List<OrderEntity> orders = userEntity.getOrders();
        if (orders == null || orders.isEmpty()) {
            return;
        }

        Optional<OrderEntity> optional = orders.stream().filter(o -> o.getId().equals(orderId)).findFirst();
        if (optional.isEmpty()) {
            return;
        }

        DynamoDbTable<UserEntity> table = getUserEntityDynamoDbTable();
        OrderEntity order = optional.get();
        order.setStatus(status);
        order.setMessage(reason);
        table.putItem(userEntity);
    }

    private DynamoDbTable<UserEntity> getUserEntityDynamoDbTable() {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .build()
                .table(System.getProperty("USER_TABLE"), TableSchema.fromBean(UserEntity.class));
    }
}
