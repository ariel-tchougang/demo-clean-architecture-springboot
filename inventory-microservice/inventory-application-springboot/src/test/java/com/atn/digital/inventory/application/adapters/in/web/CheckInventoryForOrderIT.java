package com.atn.digital.inventory.application.adapters.in.web;

import com.atn.digital.inventory.application.OutboundAdaptersExtension;
import com.atn.digital.inventory.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.inventory.application.adapters.in.rabbitmq.StockEventRabbitMQReceiver;
import com.atn.digital.inventory.application.config.InventoryDomainConfig;
import com.atn.digital.inventory.domain.models.OrderItemData;
import com.atn.digital.inventory.domain.ports.out.notifications.InsufficientStockForOrderEvent;
import com.atn.digital.inventory.domain.ports.out.notifications.SufficientStockForOrderEvent;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
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
@Import({ InventoryDomainConfig.class, RabbitMQConfiguration.class })
@ExtendWith(OutboundAdaptersExtension.class)
class CheckInventoryForOrderIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StockEventRabbitMQReceiver receiver;

    private SimpleMessageListenerContainer container;

    private final Gson gson = new Gson();



    @Test
    @Timeout(15)
    void shouldSendSufficientStockForOrderEventWhenSufficientStock() {

        CheckInventoryForOrderWeb webInput = new CheckInventoryForOrderWeb(
                "orderId",
                Arrays.asList(new OrderItemData("1", "item1", 5, BigDecimal.ONE),
                        new OrderItemData("2", "item2", 1, BigDecimal.TEN))
        );

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.container(message -> {
                String body = new String(message.getBody());
                System.out.println("Received message body: " + body);

                SufficientStockForOrderEvent event = gson.fromJson(body, SufficientStockForOrderEvent.class);
                Assertions.assertNotNull(event);
                Assertions.assertEquals(webInput.orderId(), event.orderId());

                latch.countDown();
            });
            container.start();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<CheckInventoryForOrderWeb> request = new HttpEntity<>(webInput, headers);

            ResponseEntity<Boolean> response =  restTemplate.exchange(
                    "/api/v1/inventory/check",
                    HttpMethod.POST,
                    request,
                    Boolean.class
            );

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertTrue(response.getBody());

            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }


    @Test
    @Timeout(15)
    void shouldSendInsufficientStockForOrderEventWhenInsufficientStock() {

        CheckInventoryForOrderWeb webInput = new CheckInventoryForOrderWeb(
                "orderId",
                Arrays.asList(new OrderItemData("1", "item1", 15, BigDecimal.ONE),
                        new OrderItemData("2", "item2", 20, BigDecimal.TEN))
        );

        try {
            // Register a latch to wait for the message to be received
            CountDownLatch latch = new CountDownLatch(1);
            container = receiver.container(message -> {
                String body = new String(message.getBody());
                System.out.println("Received message body: " + body);

                InsufficientStockForOrderEvent event = gson.fromJson(body, InsufficientStockForOrderEvent.class);
                Assertions.assertNotNull(event);
                Assertions.assertEquals(webInput.orderId(), event.orderId());
                Assertions.assertIterableEquals(webInput.items(), event.items());

                latch.countDown();
            });
            container.start();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<CheckInventoryForOrderWeb> request = new HttpEntity<>(webInput, headers);

            ResponseEntity<Boolean> response =  restTemplate.exchange(
                    "/api/v1/inventory/check",
                    HttpMethod.POST,
                    request,
                    Boolean.class
            );

            Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
            Assertions.assertNotNull(response.getBody());
            Assertions.assertFalse(response.getBody());

            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    void shouldReturnBadRequestWhenWebInputIsNull() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<CheckInventoryForOrderWeb> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/inventory/check",
                HttpMethod.POST,
                request,
                String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        System.out.println(response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenWebInputIsNotValid() {
        CheckInventoryForOrderWeb webInput = new CheckInventoryForOrderWeb("", new ArrayList<>());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<CheckInventoryForOrderWeb> request = new HttpEntity<>(webInput, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/inventory/check",
                HttpMethod.POST,
                request,
                String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        System.out.println(response.getBody());
    }

    @AfterEach
    void tearDown() {
        if (container != null) {
            container.stop();
        }
    }
}
