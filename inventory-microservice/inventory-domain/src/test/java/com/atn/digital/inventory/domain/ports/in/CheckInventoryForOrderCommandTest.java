package com.atn.digital.inventory.domain.ports.in;

import com.atn.digital.inventory.domain.models.OrderItemData;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderCommand;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CheckInventoryForOrderCommandTest {

    @Nested
    class OrderIdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsNull() {
            List<OrderItemData> items = Arrays.asList(new OrderItemData("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CheckInventoryForOrderCommand(null, items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsEmpty() {
            List<OrderItemData> items = Arrays.asList(new OrderItemData("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CheckInventoryForOrderCommand("", items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsBlank() {
            List<OrderItemData> items = Arrays.asList(new OrderItemData("1", "name", 1, BigDecimal.ONE));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CheckInventoryForOrderCommand(" ", items));
        }
    }

    @Nested
    class ItemsTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenItemsIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CheckInventoryForOrderCommand("customerId", null));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenItemsIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CheckInventoryForOrderCommand("", new ArrayList<>()));
        }
    }
}
