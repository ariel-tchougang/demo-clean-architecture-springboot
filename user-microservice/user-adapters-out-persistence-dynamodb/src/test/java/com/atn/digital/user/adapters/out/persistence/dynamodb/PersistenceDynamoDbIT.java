package com.atn.digital.user.adapters.out.persistence.dynamodb;

import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.models.OrderItem;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderCommand;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderUseCase;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserCommand;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserUseCase;
import com.atn.digital.user.domain.services.AddNewOrderService;
import com.atn.digital.user.domain.services.RegisterNewUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.math.BigDecimal;
import java.util.Arrays;

@Testcontainers
class PersistenceDynamoDbIT {

    private String dynamoDbLocalUri;

    @Container
    GenericContainer dynamoDBLocal =
            new GenericContainer("amazon/dynamodb-local:latest")
                    .withExposedPorts(8000);

    @BeforeEach
    public void setUp() {
        System.setProperty("USER_TABLE", "Users");
        dynamoDbLocalUri = "http://localhost:" + dynamoDBLocal.getFirstMappedPort();
    }

    @Test
    void shouldAddNewOrderForUserInTable() {
        DynamoDbClient client = DynamoDbTableCreator.initialize(dynamoDbLocalUri);
        DynamoDbUserRepository userRepository = new DynamoDbUserRepository(client);
        RegisterNewUserUseCase newUserService = new RegisterNewUserService(userRepository);
        RegisterNewUserCommand newUserCmd = new RegisterNewUserCommand(
                "Homer",
                "Simpson",
                "homer.simpson@unit.test"

        );

        var item = newUserService.handle(newUserCmd);
        Assertions.assertNotNull(item);
        Assertions.assertNotNull(item.getId());

        DynamoDbOrderRepository orderRepository = new DynamoDbOrderRepository(userRepository, client);
        AddNewOrderUseCase service = new AddNewOrderService(orderRepository);
        AddNewOrderCommand command =
                new AddNewOrderCommand("orderId", item.getId(), "address",
                        Arrays.asList(new OrderItem("1", "product1", 10)),
                        BigDecimal.TEN);
        service.handle(command);

        Order order = orderRepository.findOrdersByUserId(command.getUserId()).stream()
                .filter(o ->  command.getOrderId().equals(o.id()))
                .findFirst().orElse(null);

        Assertions.assertNotNull(order);
        Assertions.assertEquals(item.getId(), order.userId());
        Assertions.assertEquals(command.getOrderId(), order.id());
        Assertions.assertEquals("CREATED", order.status());
    }

    @Test
    void shouldUpdateOrderStatusInTable() {
        DynamoDbClient client = DynamoDbTableCreator.initialize(dynamoDbLocalUri);
        DynamoDbUserRepository userRepository = new DynamoDbUserRepository(client);
        RegisterNewUserUseCase newUserService = new RegisterNewUserService(userRepository);
        RegisterNewUserCommand newUserCmd = new RegisterNewUserCommand(
                "Homer",
                "Simpson",
                "homer.simpson@unit.test"

        );

        var item = newUserService.handle(newUserCmd);
        Assertions.assertNotNull(item);
        Assertions.assertNotNull(item.getId());

        DynamoDbOrderRepository orderRepository = new DynamoDbOrderRepository(userRepository, client);
        AddNewOrderUseCase service = new AddNewOrderService(orderRepository);
        AddNewOrderCommand command =
                new AddNewOrderCommand("orderId", item.getId(), "address",
                        Arrays.asList(new OrderItem("1", "product1", 10)),
                        BigDecimal.TEN);
        service.handle(command);

        Order order = orderRepository.findOrdersByUserId(command.getUserId()).stream()
                .filter(o ->  command.getOrderId().equals(o.id()))
                .findFirst().orElse(null);

        Assertions.assertNotNull(order);

        orderRepository.updateStatus(order.userId(), order.id(), "NEW_STATUS", "Test");
        Assertions.assertEquals(item.getId(), order.userId());
        Assertions.assertEquals(command.getOrderId(), order.id());

        order = orderRepository.findOrdersByUserId(command.getUserId()).stream()
                .filter(o ->  command.getOrderId().equals(o.id()))
                .findFirst().orElse(null);

        Assertions.assertNotNull(order);
        Assertions.assertEquals("NEW_STATUS", order.status());
        Assertions.assertEquals("Test", order.message());
    }
}
