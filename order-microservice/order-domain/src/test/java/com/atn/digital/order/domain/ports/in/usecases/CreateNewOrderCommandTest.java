package com.atn.digital.order.domain.ports.in.usecases;

import com.atn.digital.order.domain.models.OrderItem;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CreateNewOrderCommandTest {

    @Nested
    class OrderIdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsNull() {
            List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewOrderCommand(null, "address", items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsEmpty() {
            List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewOrderCommand("", "address", items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsBlank() {
            List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewOrderCommand(" ", "address", items));
        }
    }

    @Nested
    class DeliveryAddressTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsNull() {
            List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewOrderCommand("customerId", null, items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsEmpty() {
            List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewOrderCommand("customerId", "", items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsBlank() {
            List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewOrderCommand("customerId", " ", items));
        }
    }

    @Nested
    class ItemsTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenItemsIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewOrderCommand("customerId", "address", null));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenItemsIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewOrderCommand("customerId", "address", new ArrayList<>()));
        }
    }
}
