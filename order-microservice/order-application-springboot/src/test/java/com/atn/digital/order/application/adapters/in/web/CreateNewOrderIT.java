package com.atn.digital.order.application.adapters.in.web;

import com.atn.digital.order.application.OutboundAdaptersExtension;
import com.atn.digital.order.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.order.application.adapters.in.rabbitmq.OrderEventRabbitMQReceiver;
import com.atn.digital.order.application.adapters.in.web.CreateNewOrderWeb.NewOrderItemWeb;
import com.atn.digital.order.application.config.OrderDomainConfig;
import com.atn.digital.order.domain.ports.out.notifications.OrderCreatedEvent;
import com.atn.digital.order.domain.ports.out.notifications.OrderItemData;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Import({ OrderDomainConfig.class, RabbitMQConfiguration.class })
@ExtendWith(OutboundAdaptersExtension.class)
class CreateNewOrderIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private OrderEventRabbitMQReceiver receiver;

    private SimpleMessageListenerContainer container;

    private final Gson gson = new Gson();

    @Test
    void shouldCreateNewOrderWhenWebInputIsValid() {

        CreateNewOrderWeb webInput = new CreateNewOrderWeb(
                "customerId",
                "deliveryAddress",
                Arrays.asList(new NewOrderItemWeb("1", "name", 1, BigDecimal.ONE))
        );

        // Register a latch to wait for the message to be received
        CountDownLatch latch = new CountDownLatch(1);
        container = receiver.orderCreatedContainer(message -> {
            String body = new String(message.getBody());
            System.out.println("Received message body: " + body);

            OrderCreatedEvent event = gson.fromJson(body, OrderCreatedEvent.class);
            Assertions.assertNotNull(event);
            Assertions.assertEquals(webInput.getCustomerId(), event.userId());
            Assertions.assertEquals(webInput.getDeliveryAddress(), event.deliveryAddress());
            Assertions.assertIterableEquals(
                    Arrays.asList(new OrderItemData("1", "name", 1, BigDecimal.ONE)),
                    event.items()
            );

            latch.countDown();
        });
        container.start();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<CreateNewOrderWeb> request = new HttpEntity<>(webInput, headers);

        ResponseEntity<OrderIdDto> response =  restTemplate.exchange(
                "/api/v1/orders",
                HttpMethod.POST,
                request,
                OrderIdDto.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().id());

        try {
            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    void shouldReturnBadRequestWhenWebInputIsNull() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<CreateNewOrderWeb> request = new HttpEntity<>(null, headers);

        ResponseEntity<Object> response =  restTemplate.exchange(
                "/api/v1/orders",
                HttpMethod.POST,
                request,
                Object.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenWebInputIsNotValid() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        CreateNewOrderWeb webInput = new CreateNewOrderWeb("","", new ArrayList<>());
        HttpEntity<CreateNewOrderWeb> request = new HttpEntity<>(webInput, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/orders",
                HttpMethod.POST,
                request,
                String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @AfterEach
    void tearDown() {
        if (container != null) {
            container.stop();
        }
    }
}
