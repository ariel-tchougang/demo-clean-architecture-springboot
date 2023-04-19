package com.atn.digital.order.adapters.out.persistence.dynamodb;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.models.OrderStatusDetails;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderCommand;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderUseCase;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusCommand;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.atn.digital.order.domain.ports.out.notifications.InMemoryOrderEventPublisher;
import com.atn.digital.order.domain.services.CreateNewOrderService;
import com.atn.digital.order.domain.services.UpdateOrderStatusService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;

@Testcontainers
class PersistenceDynamoDbIT {

    private String dynamoDbLocalUri;

    @Container
    GenericContainer dynamoDBLocal =
            new GenericContainer("amazon/dynamodb-local:latest")
                    .withExposedPorts(8000);

    @BeforeEach
    public void setUp() {
        System.setProperty("ORDER_TABLE", "Orders");
        dynamoDbLocalUri = "http://localhost:" + dynamoDBLocal.getFirstMappedPort();
    }

    @Test
    void shouldCreateNewOrderOnSuccess() {
        DynamoDbClient client = DynamoDbTableCreator.initialize(dynamoDbLocalUri);

        InMemoryOrderEventPublisher publisher = new InMemoryOrderEventPublisher();
        DynamoDbOrderRepository repository = new DynamoDbOrderRepository(client);
        CreateNewOrderUseCase useCase = new CreateNewOrderService(repository, publisher);

        CreateNewOrderCommand command = new CreateNewOrderCommand(
                "customerId",
                "deliveryAddress",
                Arrays.asList(new OrderItem("1", "name", 2, BigDecimal.ONE))
        );

        OrderId orderId = useCase.handle(command);
        Optional<Order> optional = repository.loadOrder(orderId);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(command.getCustomerId(), optional.get().getCustomerId());
        Assertions.assertEquals(command.getDeliveryAddress(), optional.get().getDeliveryAddress());
        Assertions.assertEquals(OrderStatus.CREATED, optional.get().getStatus());
        Assertions.assertEquals(1, optional.get().getItems().size());
        Assertions.assertEquals(1, optional.get().getStatusChangeHistory().size());
        Assertions.assertEquals(1, publisher.createdOrderEvents().size());
    }

    @Test
    void shouldUpdateExistingOrderOnSuccess() {
        DynamoDbClient client = DynamoDbTableCreator.initialize(dynamoDbLocalUri);

        InMemoryOrderEventPublisher publisher = new InMemoryOrderEventPublisher();
        DynamoDbOrderRepository repository = new DynamoDbOrderRepository(client);
        CreateNewOrderUseCase createUseCase = new CreateNewOrderService(repository, publisher);

        CreateNewOrderCommand createCommand = new CreateNewOrderCommand(
                "customerId",
                "deliveryAddress",
                Arrays.asList(new OrderItem("1", "name", 2, BigDecimal.ONE))
        );

        OrderId orderId = createUseCase.handle(createCommand);

        UpdateOrderStatusUseCase useCase = new UpdateOrderStatusService(repository, repository, publisher);
        UpdateOrderStatusCommand command =
                new UpdateOrderStatusCommand(orderId.getId(), OrderStatus.PAYMENT_COMPLETED, "payment");
        OrderStatusDetails details = useCase.handle(command);
        Assertions.assertEquals(OrderStatus.PAYMENT_COMPLETED, details.status());

        Optional<Order> optional = repository.loadOrder(orderId);
        Assertions.assertEquals(2, optional.get().getStatusChangeHistory().size());
    }
}
