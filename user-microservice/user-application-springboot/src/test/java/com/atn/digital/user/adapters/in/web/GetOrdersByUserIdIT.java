package com.atn.digital.user.adapters.in.web;

import com.atn.digital.user.OutboundAdaptersExtension;
import com.atn.digital.user.config.OrderDomainConfig;
import com.atn.digital.user.config.UserDomainConfig;
import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.models.User;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderCommand;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderUseCase;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserCommand;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Import({ UserDomainConfig.class, OrderDomainConfig.class })
@ExtendWith(OutboundAdaptersExtension.class)
class GetOrdersByUserIdIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegisterNewUserUseCase newUserUseCase;

    @Autowired
    private AddNewOrderUseCase newOrderUseCase;
    @Test
    void shouldReturnNotFoundWhenPathParamIsNull() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/users/{userId}/orders",
                HttpMethod.GET,
                request,
                String.class,
                (String)null
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnNotFoundWhenPathParamIsBlank() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/users/{userId}/orders",
                HttpMethod.GET,
                request,
                String.class,
                ""
        );
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void shouldReturnEmptyListWhenUserIdIsNotFound() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        ParameterizedTypeReference<List<Order>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Order>> response =  restTemplate.exchange(
                "/api/v1/users/{userId}/orders",
                HttpMethod.GET,
                request,
                typeRef,
                "userId"
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().isEmpty());
    }

    @Test
    void shouldReturnListOfOrdersWhenUserIdIsExists() {
        RegisterNewUserCommand newUserCmd = new RegisterNewUserCommand(
                "Homer",
                "Simpson",
                "homer.simpson@unit.test"
        );

        User.UserId userId = newUserUseCase.handle(newUserCmd);
        Assertions.assertNotNull(userId);
        Assertions.assertNotNull(userId.getId());

        AddNewOrderCommand newOrderCmd = new AddNewOrderCommand(
                "orderId",
                userId.getId(),
                "address",
                new ArrayList<>(),
                BigDecimal.ZERO);
        newOrderUseCase.handle(newOrderCmd);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);
        ParameterizedTypeReference<List<Order>> typeRef = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Order>> response =  restTemplate.exchange(
                "/api/v1/users/{userId}/orders",
                HttpMethod.GET,
                request,
                typeRef,
                userId.getId()
        );
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertFalse(response.getBody().isEmpty());
        Assertions.assertEquals(1, response.getBody().size());
        Order order = response.getBody().get(0);
        Assertions.assertEquals(newOrderCmd.getOrderId(), order.id());
        Assertions.assertEquals(userId.getId(), order.userId());
    }
}
