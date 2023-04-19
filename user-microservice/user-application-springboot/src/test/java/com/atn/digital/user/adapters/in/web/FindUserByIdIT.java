package com.atn.digital.user.adapters.in.web;

import com.atn.digital.user.OutboundAdaptersExtension;
import com.atn.digital.user.config.OrderDomainConfig;
import com.atn.digital.user.config.UserDomainConfig;
import com.atn.digital.user.domain.models.User.UserId;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserCommand;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Import({ UserDomainConfig.class, OrderDomainConfig.class })
@ExtendWith(OutboundAdaptersExtension.class)
class FindUserByIdIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private RegisterNewUserUseCase newUserUseCase;
    @Test
    void shouldFindUserByIdWhenUserIdExists() {
        RegisterNewUserCommand command = new RegisterNewUserCommand(
                "Homer",
                "Simpson",
                "homer.simpson@unit.test"
        );

        UserId userId = newUserUseCase.handle(command);
        Assertions.assertNotNull(userId);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<UserDto> response =  restTemplate.exchange(
                "/api/v1/users/{userId}",
                HttpMethod.GET,
                request,
                UserDto.class,
                userId.getId()
        );

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDto user = response.getBody();
        Assertions.assertNotNull(user);
        Assertions.assertEquals(userId.getId(), user.id());
        Assertions.assertEquals(command.getFirstName(), user.firstName());
        Assertions.assertEquals(command.getLastName(), user.lastName());
        Assertions.assertEquals(command.getEmail(), user.email());
    }

    @Test
    void shouldReturnNotFoundWhenUserIdDoesntExist() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Void> request = new HttpEntity<>(null, headers);

        ResponseEntity<String> response =  restTemplate.exchange(
                "/api/v1/users/{userId}",
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
                "/api/v1/users/{userId}",
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
                "/api/v1/users/{userId}",
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
                "/api/v1/users/{userId}",
                HttpMethod.GET,
                request,
                String.class,
                "   "
        );

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}
