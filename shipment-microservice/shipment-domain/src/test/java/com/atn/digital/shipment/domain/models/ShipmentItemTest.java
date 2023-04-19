package com.atn.digital.shipment.domain.models;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShipmentItemTest {

    @Nested
    class IdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenIdIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem(null, "name", 1));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenIdIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem("", "name", 1));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenIdIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem(" ", "name", 1));
        }
    }

    @Nested
    class NameTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenNameIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem("id", null, 1));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenNameIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem("id", "", 1));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenNameIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem("id", " ", 1));
        }
    }

    @Nested
    class QuantityTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenQuantityIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem("id", "name", null));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenQuantityIsZero() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem("id", "name", 0));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenQuantityIsNegative() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new ShipmentItem("id", "name", -1));
        }
    }
}
