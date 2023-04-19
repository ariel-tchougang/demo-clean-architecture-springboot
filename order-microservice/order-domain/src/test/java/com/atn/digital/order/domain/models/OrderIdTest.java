package com.atn.digital.order.domain.models;

import com.atn.digital.order.domain.models.Order.OrderId;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderIdTest {

    @Test
    void shouldThrowConstraintViolationExceptionWhenUuidIsNull() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new OrderId(null));
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenUuidIsEmpty() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new OrderId(""));
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenUuidIsBlank() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new OrderId(" "));
    }
}
