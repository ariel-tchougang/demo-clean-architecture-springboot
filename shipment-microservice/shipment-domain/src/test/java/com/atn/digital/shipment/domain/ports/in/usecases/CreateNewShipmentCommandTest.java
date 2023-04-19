package com.atn.digital.shipment.domain.ports.in.usecases;

import com.atn.digital.shipment.domain.models.ShipmentItem;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CreateNewShipmentCommandTest {

    @Nested
    class OrderIdTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsNull() {
            List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewShipmentCommand(null, "address", items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsEmpty() {
            List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewShipmentCommand("", "address", items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenOrderIdIsBlank() {
            List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewShipmentCommand(" ", "address", items));
        }
    }

    @Nested
    class DeliveryAddressTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsNull() {
            List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewShipmentCommand("orderId", null, items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsEmpty() {
            List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewShipmentCommand("orderId", "", items));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenDeliveryAddressIsBlank() {
            List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewShipmentCommand("orderId", " ", items));
        }
    }

    @Nested
    class ItemsTest {
        @Test
        void shouldThrowConstraintViolationExceptionWhenItemsIsNull() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewShipmentCommand("orderId", "address", null));
        }

        @Test
        void shouldThrowConstraintViolationExceptionWhenItemsIsEmpty() {
            Assertions.assertThrows(ConstraintViolationException.class,
                    () -> new CreateNewShipmentCommand("", "address", new ArrayList<>()));
        }
    }
}
