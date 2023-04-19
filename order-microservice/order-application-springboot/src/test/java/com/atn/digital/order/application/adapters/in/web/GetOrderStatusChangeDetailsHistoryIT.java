package com.atn.digital.order.application.adapters.in.web;

import com.atn.digital.order.application.OutboundAdaptersExtension;
import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.models.OrderStatusChangeDetails;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderCommand;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ExtendWith(OutboundAdaptersExtension.class)
class GetOrderStatusChangeDetailsHistoryIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CreateNewOrderUseCase newOrderUseCase;

    @Test
    void shouldGetOrderStatusChangeDetailsHistoryWhenOrderIdExists() {
        CreateNewOrderCommand command = new CreateNewOrderCommand(
                "customerId",
                "deliveryAddress",
                Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE))
        );

        Order.OrderId orderId = newOrderUseCase.handle(command);
        Assertions.assertNotNull(orderId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        ParameterizedTypeReference<List<OrderStatusChangeDetails>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<OrderStatusChangeDetails>> response =  restTemplate.exchange(
                "/api/v1/orders/status/history/{orderId}",
                HttpMethod.GET,
                request,
                typeRef,
                orderId.getId()
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        List<OrderStatusChangeDetails> history = response.getBody();
        Assertions.assertNotNull(history);
        Assertions.assertFalse(history.isEmpty());
        OrderStatus status = history.get(0).newStatus();
        Assertions.assertEquals(OrderStatus.CREATED.name(), status.name());
    }

    @Test
    void shouldReturnNotFoundWhenOrderIdDoesntExist() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/orders/status/history/{orderId}",
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
                "/api/v1/orders/status/history/{orderId}",
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
                "/api/v1/orders/status/history/{orderId}",
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
                "/api/v1/orders/status/history/{orderId}",
                HttpMethod.GET,
                request,
                String.class,
                "   "
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}
