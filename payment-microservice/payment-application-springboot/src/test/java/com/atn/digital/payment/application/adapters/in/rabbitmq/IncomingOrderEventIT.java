package com.atn.digital.payment.application.adapters.in.rabbitmq;

import com.atn.digital.payment.application.OutboundAdaptersExtension;
import com.atn.digital.payment.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.payment.application.config.PaymentDomainConfig;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentCompletedEvent;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentFailedEvent;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Import({ PaymentDomainConfig.class, RabbitMQConfiguration.class })
@ExtendWith(OutboundAdaptersExtension.class)
class IncomingOrderEventIT {

    @Autowired
    private OrderEventRabbitMQSender sender;

    @Autowired
    private PaymentEventRabbitMQReceiver receiver;

    private final Gson gson = new Gson();
    private SimpleMessageListenerContainer container;

    @Test
    @Timeout(15)
    void mayReceiveOrderPaymentCompletedEventAfterInventoryCheckOk() {

        OrderStatusChangedEvent orderStatusChangedEvent = new OrderStatusChangedEvent(
                "orderId", "customerId", "deliveryAddress",
                Arrays.asList(
                        new OrderItemData("1", "item1", 5, BigDecimal.ONE),
                        new OrderItemData("2", "item2", 5, BigDecimal.TEN)),
                "CREATED", "INVENTORY_CHECK_OK", "Test"
        );

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.paymentCompletedContainer(message -> {
                String body = new String(message.getBody());
                System.out.println("Received message body: " + body);

                PaymentCompletedEvent event = gson.fromJson(body, PaymentCompletedEvent.class);
                Assertions.assertNotNull(event);
                Assertions.assertEquals(orderStatusChangedEvent.orderId(), event.orderId());

                latch.countDown();
            });
            container.start();

            sender.publish(orderStatusChangedEvent);

            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    @Timeout(15)
    void mayReceiveOrderPaymentFailedEventAfterInventoryCheckOk() {

        OrderStatusChangedEvent orderStatusChangedEvent = new OrderStatusChangedEvent(
                "orderId", "customerId", "deliveryAddress",
                Arrays.asList(
                        new OrderItemData("1", "item1", 5, BigDecimal.ONE),
                        new OrderItemData("2", "item2", 10, BigDecimal.TEN)),
                "CREATED", "INVENTORY_CHECK_OK", "Test"
        );

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.paymentFailedContainer(message -> {
                String body = new String(message.getBody());
                System.out.println("Received message body: " + body);

                PaymentFailedEvent event = gson.fromJson(body, PaymentFailedEvent.class);
                Assertions.assertNotNull(event);
                Assertions.assertEquals(orderStatusChangedEvent.orderId(), event.orderId());

                latch.countDown();
            });
            container.start();

            sender.publish(orderStatusChangedEvent);

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
