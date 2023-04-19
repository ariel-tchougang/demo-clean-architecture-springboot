package com.atn.digital.inventory.application;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.RabbitMQContainer;

import java.util.HashMap;

public class OutboundAdaptersExtension implements BeforeAllCallback, AfterAllCallback {

    private RabbitMQContainer rabbitMQContainer;

    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        rabbitMQContainer = new RabbitMQContainer("rabbitmq:latest")
                .withExchange("application_exchange", "topic", false, false, false, new HashMap<>());
        rabbitMQContainer.start();

        System.setProperty("rabbitmq.host.uri", rabbitMQContainer.getAmqpUrl());
        System.setProperty("rabbitmq.host.username", "");
        System.setProperty("rabbitmq.host.password", "");

        System.setProperty("rabbitmq.inventory.exchangeName", "application_exchange");
        System.setProperty("rabbitmq.inventory.exchangeType", "topic");
        System.setProperty("rabbitmq.inventory.sufficientStockQueueName", "sufficient_stock_queue");
        System.setProperty("rabbitmq.inventory.sufficientStockRoutingKey", "sufficient-stock-event");
        System.setProperty("rabbitmq.inventory.insufficientStockQueueName", "insufficient_stock_queue");
        System.setProperty("rabbitmq.inventory.insufficientStockRoutingKey", "insufficient-stock-event");

        System.setProperty("rabbitmq.order.exchangeName", "application_exchange");
        System.setProperty("rabbitmq.order.exchangeType", "topic");
        System.setProperty("rabbitmq.order.orderCreatedQueueName", "order_created_queue");
        System.setProperty("rabbitmq.order.orderCreatedRoutingKey", "order-created-event");
    }

    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if (rabbitMQContainer != null) {
            rabbitMQContainer.stop();
        }
    }
}
