package com.atn.digital.shipment.application.adapters.in.rabbitmq;

import com.atn.digital.shipment.application.OutboundAdaptersExtension;
import com.atn.digital.shipment.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.shipment.application.config.ShipmentDomainConfig;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentCreatedEvent;
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
@Import({ ShipmentDomainConfig.class, RabbitMQConfiguration.class })
@ExtendWith(OutboundAdaptersExtension.class)
class IncomingOrderEventIT {

    @Autowired
    private OrderEventRabbitMQSender sender;

    @Autowired
    private ShipmentEventRabbitMQReceiver receiver;

    private final Gson gson = new Gson();
    private SimpleMessageListenerContainer container;

    @Test
    @Timeout(15)
    void shouldCreateShipmentWhenReceivingOrderPaymentCompletedEvent() {

        OrderStatusChangedEvent orderStatusChangedEvent = new OrderStatusChangedEvent(
                "orderId", "customerId", "deliveryAddress",
                Arrays.asList(
                        new OrderItemData("1", "item1", 5, BigDecimal.ONE),
                        new OrderItemData("2", "item2", 1, BigDecimal.TEN)),
                "CREATED", "PAYMENT_COMPLETED", "Test"
        );

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.shipmentCreatedContainer(message -> {
                String body = new String(message.getBody());
                System.out.println("Received message body: " + body);

                ShipmentCreatedEvent event = gson.fromJson(body, ShipmentCreatedEvent.class);
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
