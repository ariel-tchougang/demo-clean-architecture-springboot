package com.atn.digital.commons.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public abstract class SelfValidating<T> {

    private Validator validator;
    private static ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public SelfValidating() {
        validator = factory.getValidator();
    }

    /**
     * Evaluates all Bean Validations on the attributes of this
     * instance.
     */
    protected void validateSelf() {
        Set<ConstraintViolation<T>> violations = validator.validate((T) this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    protected <T extends Number> void requireStrictPositiveValue(@NotNull String varName, @NotNull T value) {
        if (value.doubleValue() <= 0) {
            throw new ConstraintViolationException(varName + " expected to be > 0", null);
        }
    }

    protected <T extends Number> void requirePositiveValue(@NotNull String varName, @NotNull T value) {
        if (value.doubleValue() < 0) {
            throw new ConstraintViolationException(varName + " expected to be >= 0", null);
        }
    }
}
