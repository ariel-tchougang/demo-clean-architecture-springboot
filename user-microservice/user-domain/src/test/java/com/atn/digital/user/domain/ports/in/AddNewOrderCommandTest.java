package com.atn.digital.user.domain.ports.in;

import com.atn.digital.user.domain.models.OrderItem;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderCommand;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class AddNewOrderCommandTest {

    private final List<OrderItem> emptyList = new ArrayList<>();

    @Nested
    class OrderIdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand(null, "userId", "address", emptyList, BigDecimal.ZERO));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand("", "userId", "address", emptyList, BigDecimal.ZERO));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand(" ", "userId", "address", emptyList, BigDecimal.ZERO));
        }
    }

    @Nested
    class UserIdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenUserIdIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand("orderId", null, "address", emptyList, BigDecimal.ZERO));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenUserIdIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand("orderId", "", "address", emptyList, BigDecimal.ZERO));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenUserIdIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand("orderId", " ", "address", emptyList, BigDecimal.ZERO));
        }
    }

    @Nested
    class DeliveryAddressTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand("orderId", "userId", null, emptyList, BigDecimal.ZERO));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand("orderId", "userId", "", emptyList, BigDecimal.ZERO));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsNotValid() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new AddNewOrderCommand("orderId", "userId", " ", emptyList, BigDecimal.ZERO));
        }
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenItemListIsNull() {
        Assertions.assertThrows(ConstraintViolationException.class,
                () -> new AddNewOrderCommand("orderId", "userId", "address", null, BigDecimal.ZERO));
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenItemAmountIsNull() {
        Assertions.assertThrows(ConstraintViolationException.class,
                () -> new AddNewOrderCommand("orderId", "userId", "address", emptyList, null));
    }
}
