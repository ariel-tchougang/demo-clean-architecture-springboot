package com.atn.digital.user.domain.ports.in;

import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserCommand;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterNewUserCommandTest {

    @Nested
    class FirstNameTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenFirstNameIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand(null, "Last", "email@unit.test"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenFirstNameIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand("", "Last", "email@unit.test"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenFirstNameIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand(" ", "Last", "email@unit.test"));
        }
    }

    @Nested
    class LastNameTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenLastNameIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand("First", null, "email@unit.test"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenLastNameIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand("First", "", "email@unit.test"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenLastNameIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand("First", " ", "email@unit.test"));
        }
    }

    @Nested
    class EmailTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenEmailIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand("First", "Last", null));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenEmailIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand("First", "Last", ""));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenEmailIsNotValid() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new RegisterNewUserCommand("First", "Last", " "));
        }
    }
}
