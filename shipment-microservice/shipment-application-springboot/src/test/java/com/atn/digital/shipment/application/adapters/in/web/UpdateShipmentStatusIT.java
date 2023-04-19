package com.atn.digital.shipment.application.adapters.in.web;

import com.atn.digital.shipment.application.OutboundAdaptersExtension;
import com.atn.digital.shipment.application.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.shipment.application.adapters.in.rabbitmq.ShipmentEventRabbitMQReceiver;
import com.atn.digital.shipment.application.config.ShipmentDomainConfig;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import com.atn.digital.shipment.domain.models.ShipmentStatus;
import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentUseCase;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentStatusChangedEvent;
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

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(OutboundAdaptersExtension.class)
@Import({ ShipmentDomainConfig.class, RabbitMQConfiguration.class })
@DirtiesContext
class UpdateShipmentStatusIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CreateNewShipmentUseCase createUseCase;

    @Autowired
    private ShipmentEventRabbitMQReceiver receiver;

    private SimpleMessageListenerContainer container;

    private final Gson gson = new Gson();

    @Test
    void shouldChangeStatusWhenShipmentIdExists() {
        CreateNewShipmentCommand command = new CreateNewShipmentCommand(
                "orderId",
                "deliveryAddress",
                Arrays.asList(new ShipmentItem("1", "item", 1))
        );

        // Register a latch to wait for the message to be received
        CountDownLatch latch = new CountDownLatch(1);
        container = receiver.shipmentStatusChangedContainer(message -> {
            String body = new String(message.getBody());
            System.out.println("Received message body: " + body);

            ShipmentStatusChangedEvent event = gson.fromJson(body, ShipmentStatusChangedEvent.class);
            Assertions.assertNotNull(event);
            Assertions.assertEquals(command.getOrderId(), event.orderId());

            latch.countDown();
        });
        container.start();

        ShipmentId shipmentId = createUseCase.handle(command);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        UpdateShipmentStatusWeb webInput = new UpdateShipmentStatusWeb(
                shipmentId.getId(),
                ShipmentStatus.DELIVERED,
                "Delivered"
        );
        HttpEntity<UpdateShipmentStatusWeb> request = new HttpEntity<>(webInput, headers);

        ResponseEntity<ShipmentStatusDetails> response =  restTemplate.exchange(
                "/api/v1/shipments/status",
                HttpMethod.POST,
                request,
                ShipmentStatusDetails.class
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(webInput.getNewStatus(), response.getBody().status());
        Assertions.assertEquals(webInput.getReason(), response.getBody().reason());

        try {
            // Wait for the latch to be counted down, indicating that the message was received
            assertThat(latch.await(10, TimeUnit.SECONDS)).isTrue();
        } catch (Exception e) {
            Assertions.fail(e.getMessage(), e);
        }
    }

    @Test
    void shouldReturnNullWhenShipmentIdDoesntExist() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        UpdateShipmentStatusWeb webInput = new UpdateShipmentStatusWeb(
                "orderId",
                ShipmentStatus.DELIVERED,
                "Delivered"
        );
        HttpEntity<UpdateShipmentStatusWeb> request = new HttpEntity<>(webInput, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/shipments/status",
                HttpMethod.POST,
                request,
                String.class
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenWebInputIsInvalid() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<UpdateShipmentStatusWeb> request = new HttpEntity<>(new UpdateShipmentStatusWeb(), headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/shipments/status",
                HttpMethod.POST,
                request,
                String.class
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenWebInputIsNull() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<UpdateShipmentStatusWeb> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/shipments/status",
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
