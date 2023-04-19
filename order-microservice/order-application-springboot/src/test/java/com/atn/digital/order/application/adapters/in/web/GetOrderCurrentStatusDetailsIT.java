package com.atn.digital.order.application.adapters.in.web;

import com.atn.digital.order.application.OutboundAdaptersExtension;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.models.OrderStatusDetails;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderCommand;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith(OutboundAdaptersExtension.class)
class GetOrderCurrentStatusDetailsIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CreateNewOrderUseCase newOrderUseCase;

    @Test
    void shouldGetCurrentStatusDetailsWhenOrderIdExists() {
        CreateNewOrderCommand command = new CreateNewOrderCommand(
                "customerId",
                "deliveryAddress",
                Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE))
        );

        OrderId orderId = newOrderUseCase.handle(command);
        Assertions.assertNotNull(orderId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<OrderStatusDetails> response =  restTemplate.exchange(
                "/api/v1/orders/status/{orderId}",
                HttpMethod.GET,
                request,
                OrderStatusDetails.class,
                orderId.getId()
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        OrderStatusDetails details = response.getBody();
        Assertions.assertNotNull(details);
        Assertions.assertEquals(OrderStatus.CREATED, details.status());
    }

    @Test
    void shouldReturnNotFoundWhenOrderIdDoesntExist() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/orders/status/{orderId}",
                HttpMethod.GET,
                request,
                String.class,
                "userId"
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenPathParamIsNull() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/orders/status/{orderId}",
                HttpMethod.GET,
                request,
                String.class,
                (String)null
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenPathParamIsEmpty() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/orders/status/{orderId}",
                HttpMethod.GET,
                request,
                String.class,
                ""
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnBadRequestWhenPathParamIsBlank() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/orders/status/{orderId}",
                HttpMethod.GET,
                request,
                String.class,
                "   "
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}
