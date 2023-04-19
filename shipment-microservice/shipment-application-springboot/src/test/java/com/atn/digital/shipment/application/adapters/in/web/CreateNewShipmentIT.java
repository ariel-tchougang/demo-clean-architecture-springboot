package com.atn.digital.shipment.application.adapters.in.web;

import com.atn.digital.shipment.application.OutboundAdaptersExtension;
import com.atn.digital.shipment.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.shipment.application.adapters.in.rabbitmq.ShipmentEventRabbitMQReceiver;
import com.atn.digital.shipment.application.adapters.in.web.CreateNewShipmentWeb.NewShipmentItemWeb;
import com.atn.digital.shipment.application.config.ShipmentDomainConfig;
import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentCreatedEvent;
import com.atn.digital.shipment.domain.ports.out.persistence.LoadShipmentPort;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutboundAdaptersExtension.class)
@Import({ ShipmentDomainConfig.class, RabbitMQConfiguration.class })
@DirtiesContext
class CreateNewShipmentIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private LoadShipmentPort loadShipmentPort;

    @Autowired
    private ShipmentEventRabbitMQReceiver receiver;

    private SimpleMessageListenerContainer container;

    private final Gson gson = new Gson();

    @Test
    void shouldCreateNewShipment() {

        CreateNewShipmentWeb webInput = new CreateNewShipmentWeb(
                "orderId",
                "deliveryAddress",
                Arrays.asList(new NewShipmentItemWeb("1", "item", 1))
        );

        // Register a latch to wait for the message to be received
        CountDownLatch latch = new CountDownLatch(1);
        container = receiver.shipmentCreatedContainer(message -> {
            String body = new String(message.getBody());
            System.out.println("Received message body: " + body);

            ShipmentCreatedEvent event = gson.fromJson(body, ShipmentCreatedEvent.class);
            Assertions.assertNotNull(event);
            Assertions.assertEquals(webInput.getOrderId(), event.orderId());

            latch.countDown();
        });
        container.start();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<CreateNewShipmentWeb> request = new HttpEntity<>(webInput, headers);

        ResponseEntity<ShipmentIdDto> response =  restTemplate.exchange(
                "/api/v1/shipments",
                HttpMethod.POST,
                request,
                ShipmentIdDto.class
        );

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ShipmentIdDto shipmentId = response.getBody();
        Assertions.assertNotNull(shipmentId);

        Optional<Shipment> optionalShipment = loadShipmentPort.loadShipment(new ShipmentId(shipmentId.id()));
        Assertions.assertTrue(optionalShipment.isPresent());

        Shipment shipment = optionalShipment.get();
        Assertions.assertTrue(shipment.getId().isPresent());
        Assertions.assertEquals(shipment.getId().get().getId(), shipmentId.id());
        Assertions.assertIterableEquals(
                Arrays.asList(new ShipmentItem("1", "item", 1)),
                shipment.getItems());

        try {
            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    void shouldReturnBadRequestWhenInputIsNull() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<CreateNewShipmentWeb> request = new HttpEntity<>(null, headers);

        ResponseEntity<ShipmentIdDto> response =  restTemplate.exchange(
                "/api/v1/shipments",
                HttpMethod.POST,
                request,
                ShipmentIdDto.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNull(response.getBody().id());
    }

    @Test
    void shouldReturnBadRequestWhenInputIsNotValid() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        CreateNewShipmentWeb webInput = new CreateNewShipmentWeb("", "", new ArrayList<>());
        HttpEntity<CreateNewShipmentWeb> request = new HttpEntity<>(webInput, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/shipments",
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
