package com.atn.digital.user.domain.models;

import com.atn.digital.user.domain.models.User.UserId;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class UserIdTest {

    @Test
    void shouldThrowConstraintViolationExceptionWhenIdIsNull() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UserId(null));
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenIdIsEmpty() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UserId(""));
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenIdIsBlank() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new UserId(" "));
    }
}
