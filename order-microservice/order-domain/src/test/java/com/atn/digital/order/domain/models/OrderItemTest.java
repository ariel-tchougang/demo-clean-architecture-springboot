package com.atn.digital.order.domain.models;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OrderItemTest {

    @Nested
    class IdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenIdIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem(null, "name", 1, BigDecimal.ONE));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenIdIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("", "name", 1, BigDecimal.ONE));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenIdIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem(" ", "name", 1, BigDecimal.ONE));
        }
    }

    @Nested
    class NameTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenNameIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("id", null, 1, BigDecimal.ONE));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenNameIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("id", "", 1, BigDecimal.ONE));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenNameIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("id", " ", 1, BigDecimal.ONE));
        }
    }

    @Nested
    class QuantityTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenQuantityIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("id", "name", null, BigDecimal.ONE));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenQuantityIsZero() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("id", "name", 0, BigDecimal.ONE));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenQuantityIsNegative() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("id", "name", -1, BigDecimal.ONE));
        }
    }

    @Nested
    class UnitaryPriceTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenUnitaryPriceIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("id", "name", 1, null));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenUnitaryPriceIsNegative() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new OrderItem("id", "name", 1, BigDecimal.ONE.negate()));
        }
    }
}
