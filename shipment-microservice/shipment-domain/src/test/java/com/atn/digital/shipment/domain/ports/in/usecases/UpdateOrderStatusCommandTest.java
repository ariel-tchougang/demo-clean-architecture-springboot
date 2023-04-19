package com.atn.digital.shipment.domain.ports.in.usecases;

import com.atn.digital.shipment.domain.models.ShipmentStatus;
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
                    () -> new UpdateShipmentStatusCommand(null, ShipmentStatus.UNKNOWN, "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateShipmentStatusCommand("", ShipmentStatus.UNKNOWN, "reason"));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateShipmentStatusCommand(" ", ShipmentStatus.UNKNOWN, "reason"));
        }
    }

    @Nested
    class NewStatusTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenNewStatusIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateShipmentStatusCommand("shipmentId", null, null));
        }
    }

    @Nested
    class ReasonTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenReasonIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateShipmentStatusCommand("shipmentId", ShipmentStatus.UNKNOWN, null));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenReasonIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateShipmentStatusCommand("shipmentId", ShipmentStatus.UNKNOWN, ""));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenReasonIsBlank() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new UpdateShipmentStatusCommand("shipmentId", ShipmentStatus.UNKNOWN, " "));
        }
    }
}
