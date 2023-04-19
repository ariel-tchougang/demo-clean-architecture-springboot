package com.atn.digital.payment.adapters.out.persistence.dynamodb;

import com.atn.digital.payment.domain.ports.in.ProcessPaymentUseCase;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;
import com.atn.digital.payment.domain.ports.out.notifications.DummyPaymentPublisher;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentCompletedEvent;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentFailedEvent;
import com.atn.digital.payment.domain.ports.out.processing.DummyPaymentOperator;
import com.atn.digital.payment.domain.ports.out.processing.PaymentFailureException;
import com.atn.digital.payment.domain.services.ProcessPaymentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.math.BigDecimal;

@Testcontainers
class PersistenceDynamoDbIT {
    
    private final String someCustomerId = "ce71c593-d51a-400f-93ab-01348aba0b4d";
    private final String someOrderId = "b1f6d93b-4ef6-4c19-a810-edbe7291dc51";

    private String dynamoDbLocalUri;

    @Container
    GenericContainer dynamoDBLocal =
            new GenericContainer("amazon/dynamodb-local:latest")
                    .withExposedPorts(8000);

    @BeforeEach
    public void setUp() {
        System.setProperty("PAYMENT_TABLE", "Payments");
        dynamoDbLocalUri = "http://localhost:" + dynamoDBLocal.getFirstMappedPort();
    }

    @Test
    void shouldSaveCompletedPaymentDetailsOnSuccess() {
        DynamoDbClient client = DynamoDbTableCreator.initialize(dynamoDbLocalUri);

        DummyPaymentOperator handler = new DummyPaymentOperator();
        DummyPaymentPublisher publisher = new DummyPaymentPublisher();
        DynamoDbPaymentRepository repository = new DynamoDbPaymentRepository(client);
        ProcessPaymentUseCase useCase = new ProcessPaymentService(handler, repository, publisher, publisher);

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                someOrderId,
                someCustomerId,
                "Test success",
                BigDecimal.TEN

        );

        useCase.handle(command);

        Assertions.assertEquals(1, publisher.completedEvents().size());
        PaymentCompletedEvent event = publisher.completedEvents().get(0);
        Assertions.assertEquals(command.orderId(), event.orderId());
    }

    @Test
    void shouldSaveFailedPaymentDetailsOnSuccess() {
        DynamoDbClient client = DynamoDbTableCreator.initialize(dynamoDbLocalUri);

        DummyPaymentOperator handler = new DummyPaymentOperator();
        DummyPaymentPublisher publisher = new DummyPaymentPublisher();
        DynamoDbPaymentRepository repository = new DynamoDbPaymentRepository(client);
        ProcessPaymentUseCase useCase = new ProcessPaymentService(handler, repository, publisher, publisher);

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                someOrderId,
                someCustomerId,
                "Test success",
                BigDecimal.TEN.negate()

        );

        Assertions.assertThrows(PaymentFailureException.class, () -> useCase.handle(command));

        Assertions.assertEquals(1, publisher.failedEvents().size());
        PaymentFailedEvent event = publisher.failedEvents().get(0);
        Assertions.assertEquals(command.orderId(), event.orderId());
    }
}
