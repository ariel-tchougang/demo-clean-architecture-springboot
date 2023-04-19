package com.atn.digital.shipment.adapters.out.persistence.dynamodb;

import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import com.atn.digital.shipment.domain.models.ShipmentStatus;
import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentUseCase;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusUseCase;
import com.atn.digital.shipment.domain.ports.out.notifications.InMemoryShipmentEventPublisher;
import com.atn.digital.shipment.domain.services.CreateNewShipmentService;
import com.atn.digital.shipment.domain.services.UpdateShipmentStatusService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

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
        System.setProperty("SHIPMENT_TABLE", "Shipments");
        dynamoDbLocalUri = "http://localhost:" + dynamoDBLocal.getFirstMappedPort();
    }

    @Test
    void shouldCreateNewShipmentOnSuccess() {
        DynamoDbClient client = DynamoDbTableCreator.initialize(dynamoDbLocalUri);

        InMemoryShipmentEventPublisher publisher = new InMemoryShipmentEventPublisher();
        DynamoDbShipmentRepository repository = new DynamoDbShipmentRepository(client);
        CreateNewShipmentUseCase useCase = new CreateNewShipmentService(repository, publisher);

        CreateNewShipmentCommand command = new CreateNewShipmentCommand(
                "orderId",
                "deliveryAddress",
                Arrays.asList(new ShipmentItem("1", "name", 2))
        );

        ShipmentId orderId = useCase.handle(command);
        Optional<Shipment> optional = repository.loadShipment(orderId);
        Assertions.assertTrue(optional.isPresent());
        Assertions.assertEquals(command.getOrderId(), optional.get().getOrderId());
        Assertions.assertEquals(command.getDeliveryAddress(), optional.get().getDeliveryAddress());
        Assertions.assertEquals(ShipmentStatus.CREATED, optional.get().getStatus());
        Assertions.assertEquals(1, optional.get().getItems().size());
        Assertions.assertEquals(1, optional.get().getStatusChangeHistory().size());
        Assertions.assertEquals(1, publisher.createdShipmentEvents().size());
    }

    @Test
    void shouldUpdateExistingShipmentOnSuccess() {
        DynamoDbClient client = DynamoDbTableCreator.initialize(dynamoDbLocalUri);

        InMemoryShipmentEventPublisher publisher = new InMemoryShipmentEventPublisher();
        DynamoDbShipmentRepository repository = new DynamoDbShipmentRepository(client);
        CreateNewShipmentUseCase createUseCase = new CreateNewShipmentService(repository, publisher);

        CreateNewShipmentCommand createCommand = new CreateNewShipmentCommand(
                "orderId",
                "deliveryAddress",
                Arrays.asList(new ShipmentItem("1", "name", 2))
        );

        ShipmentId orderId = createUseCase.handle(createCommand);

        UpdateShipmentStatusUseCase useCase = new UpdateShipmentStatusService(repository, repository, publisher);
        UpdateShipmentStatusCommand command =
                new UpdateShipmentStatusCommand(orderId.getId(), ShipmentStatus.DELIVERED, "delivered");
        ShipmentStatusDetails details = useCase.handle(command);
        Assertions.assertEquals(ShipmentStatus.DELIVERED, details.status());

        Optional<Shipment> optional = repository.loadShipment(orderId);
        Assertions.assertEquals(2, optional.get().getStatusChangeHistory().size());
    }
}
