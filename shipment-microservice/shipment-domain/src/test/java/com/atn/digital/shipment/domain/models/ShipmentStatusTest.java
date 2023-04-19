package com.atn.digital.shipment.domain.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ShipmentStatusTest {

    @Test
    void getStatusShouldReturnCreatedWhenValueIsCreated() {
        Assertions.assertEquals(ShipmentStatus.CREATED, ShipmentStatus.getStatus("CREATED"));
    }

    @Test
    void getStatusShouldReturnPaymentCompletedWhenValueIsPaymentCompleted() {
        Assertions.assertEquals(ShipmentStatus.ON_THE_WAY, ShipmentStatus.getStatus("ON_THE_WAY"));
    }

    @Test
    void getStatusShouldReturnDeliveredWhenValueIsDelivered() {
        Assertions.assertEquals(ShipmentStatus.DELIVERED, ShipmentStatus.getStatus("DELIVERED"));
    }

    @Test
    void getStatusShouldReturnCancelledWhenValueIsLost() {
        Assertions.assertEquals(ShipmentStatus.LOST, ShipmentStatus.getStatus("LOST"));
    }

    @Test
    void getStatusShouldReturnUnknownWhenValueIsNotInEnum() {
        Assertions.assertEquals(ShipmentStatus.UNKNOWN, ShipmentStatus.getStatus("baDabouM"));
    }

    @Test
    void getStatusShouldBeNonCaseSensitive() {
        Assertions.assertEquals(ShipmentStatus.LOST, ShipmentStatus.getStatus("LoSt"));
        Assertions.assertEquals(ShipmentStatus.CREATED, ShipmentStatus.getStatus("CreATeD"));
    }
}
