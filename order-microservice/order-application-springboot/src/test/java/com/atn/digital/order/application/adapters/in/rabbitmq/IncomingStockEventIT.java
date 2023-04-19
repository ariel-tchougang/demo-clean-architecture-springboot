package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.OutboundAdaptersExtension;
import com.atn.digital.order.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.order.application.config.OrderDomainConfig;
import com.atn.digital.order.domain.models.Order.OrderId;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Import({ OrderDomainConfig.class, RabbitMQConfiguration.class })
@ExtendWith(OutboundAdaptersExtension.class)
class IncomingStockEventIT {

    @Autowired
    private StockEventRabbitMQSender sender;

    @Autowired
    private OrderEventRabbitMQReceiver receiver;

    @Autowired
    private CreateNewOrderUseCase service;

    private final Gson gson = new Gson();

    private OrderId orderId;

    private SimpleMessageListenerContainer container;

    @BeforeEach
    void setup() {
        List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
        CreateNewOrderCommand command = new CreateNewOrderCommand("customerId", "address", items);
        orderId = service.handle(command);
    }

    @Test
    @Timeout(15)
    void shouldSetStatusToInventoryCheckOkWhenReceivingSufficientStockForOrderEvent() {

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.orderStatusChangedContainer(message -> {
                String body = new String(message.getBody());
                OrderStatusChangedEvent event = gson.fromJson(body, OrderStatusChangedEvent.class);

                if (orderId.getId().equals(event.orderId())) {
                    System.out.println("Received message body: " + body);
                    Assertions.assertNotNull(event);
                    Assertions.assertEquals(OrderStatus.INVENTORY_CHECK_OK.name(), event.newStatus());
                    latch.countDown();
                }
            });
            container.start();

            SufficientStockForOrderEvent sufficientStockEvent = new SufficientStockForOrderEvent(orderId.getId());
            sender.publish(sufficientStockEvent);

            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }


    @Test
    @Timeout(15)
    void shouldCancelOrderWhenInsufficientStock() {

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

            InsufficientStockForOrderEvent insufficientStockEvent =
                    new InsufficientStockForOrderEvent(orderId.getId(), new ArrayList<>());
            sender.publish(insufficientStockEvent);

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
