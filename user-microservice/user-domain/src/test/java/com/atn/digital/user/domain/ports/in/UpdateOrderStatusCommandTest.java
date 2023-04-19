package com.atn.digital.user.domain.ports.in;

import com.atn.digital.user.domain.ports.in.usecases.UpdateOrderStatusCommand;
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
                    () -> new UpdateOrderStatusCommand(null, "userId", "newStatus", "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("", "userId", "newStatus", "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand(" ", "userId", "newStatus", "reason"));
        }
    }

    @Nested
    class UserIdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenUserIdIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", null, "newStatus", "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenUserIdIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", "", "newStatus", "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenUserIdIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", " ", "newStatus", "reason"));
        }
    }

    @Nested
    class NewStatusTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenNewStatusIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", "userId", null, "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenNewStatusIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", "userId", "", "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenNewStatusIsNotValid() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateOrderStatusCommand("orderId", "userId", " ", "reason"));
        }
    }
}
