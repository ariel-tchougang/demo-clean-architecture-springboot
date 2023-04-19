package com.atn.digital.shipment.domain.models;

import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShipmentIdTest {

    @Test
    void shouldThrowConstraintViolationExceptionWhenUuidIsNull() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new ShipmentId(null));
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenUuidIsEmpty() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new ShipmentId(""));
    }

    @Test
    void shouldThrowConstraintViolationExceptionWhenUuidIsBlank() {
        Assertions.assertThrows(ConstraintViolationException.class, () -> new ShipmentId(" "));
    }
}
