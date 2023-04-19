package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.OutboundAdaptersExtension;
import com.atn.digital.order.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.order.application.config.OrderDomainConfig;
import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderCommand;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderUseCase;
import com.atn.digital.order.domain.ports.out.notifications.OrderStatusChangedEvent;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Import({ OrderDomainConfig.class, RabbitMQConfiguration.class })
@ExtendWith(OutboundAdaptersExtension.class)
class IncomingShipmentEventIT {

    @Autowired
    private ShipmentEventRabbitMQSender sender;

    @Autowired
    private OrderEventRabbitMQReceiver receiver;

    @Autowired
    private CreateNewOrderUseCase service;

    private final Gson gson = new Gson();

    private Order.OrderId orderId;

    private SimpleMessageListenerContainer container;

    @BeforeEach
    void setup() {
        List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
        CreateNewOrderCommand command = new CreateNewOrderCommand("customerId", "address", items);
        orderId = service.handle(command);
    }

    @Test
    @Timeout(15)
    void shouldSetStatusToProcessingInProgressWhenReceivingShipmentCreatedEvent() {

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.orderStatusChangedContainer(message -> {
                String body = new String(message.getBody());
                OrderStatusChangedEvent event = gson.fromJson(body, OrderStatusChangedEvent.class);

                if (orderId.getId().equals(event.orderId())) {
                    System.out.println("Received message body: " + body);
                    Assertions.assertNotNull(event);
                    Assertions.assertEquals(OrderStatus.PROCESSING_IN_PROGRESS.name(), event.newStatus());
                    latch.countDown();
                }
            });
            container.start();

            ShipmentCreatedEvent shipmentCreatedEvent =
                    new ShipmentCreatedEvent("shipmentId", orderId.getId());
            sender.publish(shipmentCreatedEvent);

            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Timeout(15)
    void shouldChangeOrderStatusToPendingDeliveryWhenShipmentIsOnTheWay() {

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.orderStatusChangedContainer(message -> {
                String body = new String(message.getBody());
                OrderStatusChangedEvent event = gson.fromJson(body, OrderStatusChangedEvent.class);

                if (orderId.getId().equals(event.orderId())) {
                    System.out.println("Received message body: " + body);
                    Assertions.assertNotNull(event);
                    Assertions.assertEquals(OrderStatus.PENDING_DELIVERY.name(), event.newStatus());
                    latch.countDown();
                }
            });
            container.start();

            ShipmentStatusChangedEvent shipmentStatusChangedEvent = new ShipmentStatusChangedEvent(
                    "shipmentId",
                    orderId.getId(),
                    "CREATED",
                    "ON_THE_WAY");
            sender.publish(shipmentStatusChangedEvent);

            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Timeout(15)
    void shouldChangeOrderStatusToDeliveredWhenShipmentIsDelivered() {

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.orderStatusChangedContainer(message -> {
                String body = new String(message.getBody());
                OrderStatusChangedEvent event = gson.fromJson(body, OrderStatusChangedEvent.class);

                if (orderId.getId().equals(event.orderId())) {
                    System.out.println("Received message body: " + body);
                    Assertions.assertNotNull(event);
                    Assertions.assertEquals(OrderStatus.DELIVERED.name(), event.newStatus());
                    latch.countDown();
                }
            });
            container.start();

            ShipmentStatusChangedEvent shipmentStatusChangedEvent = new ShipmentStatusChangedEvent(
                    "shipmentId",
                    orderId.getId(),
                    "ON_THE_WAY",
                    "DELIVERED");
            sender.publish(shipmentStatusChangedEvent);

            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }


    @Test
    @Timeout(15)
    void shouldCancelOrderWhenShipmentIsLost() {

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.orderStatusChangedContainer(message -> {
                String body = new String(message.getBody());
                OrderStatusChangedEvent event = gson.fromJson(body, OrderStatusChangedEvent.class);

                if (orderId.getId().equals(event.orderId())) {
                    System.out.println("Received message body: " + body);
                    Assertions.assertNotNull(event);
                    Assertions.assertEquals(OrderStatus.CANCELLED.name(), event.newStatus());
                    latch.countDown();
                }
            });
            container.start();

            ShipmentStatusChangedEvent shipmentStatusChangedEvent = new ShipmentStatusChangedEvent(
                    "shipmentId",
                    orderId.getId(),
                    "ON_THE_WAY",
                    "LOST");
            sender.publish(shipmentStatusChangedEvent);

            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @AfterEach
    void tearDown() {
        if (container != null) {
            container.stop();
        }
    }
}
