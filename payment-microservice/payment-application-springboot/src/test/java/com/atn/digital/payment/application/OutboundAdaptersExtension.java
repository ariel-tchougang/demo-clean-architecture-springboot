package com.atn.digital.payment.application;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.RabbitMQContainer;

import java.util.HashMap;

public class OutboundAdaptersExtension implements BeforeAllCallback, AfterAllCallback {

    private RabbitMQContainer rabbitMQContainer;
    private GenericContainer dynamoDBLocal;

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        rabbitMQContainer = new RabbitMQContainer("rabbitmq:latest")
                .withExchange("application_exchange", "topic", false, false, false, new HashMap<>());
        rabbitMQContainer.start();

        System.setProperty("rabbitmq.host.uri", rabbitMQContainer.getAmqpUrl());
        System.setProperty("rabbitmq.host.username", "");
        System.setProperty("rabbitmq.host.password", "");

        System.setProperty("rabbitmq.payment.exchangeName", "application_exchange");
        System.setProperty("rabbitmq.payment.exchangeType", "topic");
        System.setProperty("rabbitmq.payment.paymentCompletedQueueName", "payment_completed_queue");
        System.setProperty("rabbitmq.payment.paymentCompletedRoutingKey", "payment-completed-event");
        System.setProperty("rabbitmq.payment.paymentFailedQueueName", "payment_failed_queue");
        System.setProperty("rabbitmq.payment.paymentFailedRoutingKey", "payment-failed-event");

        System.setProperty("rabbitmq.order.exchangeName", "application_exchange");
        System.setProperty("rabbitmq.order.exchangeType", "topic");
        System.setProperty("rabbitmq.order.orderStatusChangedQueueName", "order_status_changed_queue");
        System.setProperty("rabbitmq.order.orderStatusChangedRoutingKey", "order-status-changed-event");

        dynamoDBLocal = new GenericContainer("amazon/dynamodb-local:latest").withExposedPorts(8000);
        dynamoDBLocal.start();
        System.setProperty("dynamodb.payments.uri", "http://localhost:" + dynamoDBLocal.getFirstMappedPort());
        System.setProperty("dynamodb.payments.tableName", "Payments");
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if (rabbitMQContainer != null) {
            rabbitMQContainer.stop();
        }

        if (dynamoDBLocal != null) {
            dynamoDBLocal.stop();
        }
    }
}
