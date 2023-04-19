package com.atn.digital.order.domain.ports.in.usecases;

import com.atn.digital.order.domain.models.OrderStatus;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class UpdateOrderStatusCommandTest {

    @Nested
    class OrderIdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand(null, OrderStatus.UNKNOWN, "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("", OrderStatus.UNKNOWN, "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand(" ", OrderStatus.UNKNOWN, "reason"));
        }
    }

    @Nested
    class NewStatusTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenNewStatusIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", null, null));
        }
    }

    @Nested
    class ReasonTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenReasonIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", OrderStatus.UNKNOWN, null));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenReasonIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", OrderStatus.UNKNOWN, ""));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenReasonIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", OrderStatus.UNKNOWN, " "));
        }
    }
}
