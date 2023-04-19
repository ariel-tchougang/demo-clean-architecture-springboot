package com.atn.digital.shipment.domain.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ShipmentTest {

    @Nested
    class OnNewShipmentInstantiation {
        @Test
        void statusIsCreated() {
            Shipment shipment = Shipment.withoutId("shipmentId", "deliveryAddress");
            Assertions.assertEquals(ShipmentStatus.CREATED, shipment.getStatus());
        }

        @Test
        void statusChangeHistoryIsInitializedWithStatusCreated() {
            Shipment shipment = Shipment.withoutId("shipmentId", "deliveryAddress");
            Assertions.assertEquals(1, shipment.getStatusChangeHistory().size());
            Assertions.assertEquals(ShipmentStatus.CREATED, shipment.getStatusChangeHistory().get(0).newStatus());
            Assertions.assertNull(shipment.getStatusChangeHistory().get(0).oldStatus());
        }

        @Test
        void statusDetailsReasonIsCreation() {
            Shipment shipment = Shipment.withoutId("shipmentId", "deliveryAddress");
            Assertions.assertEquals("CREATION", shipment.getStatusDetails().reason());
        }
    }

    @Nested
    class OnShipmentItemAdded {
        @Test
        void amountIsSumOfItemsAmounts() {
            Shipment shipment = Shipment.withoutId("shipmentId", "deliveryAddress");
            com.atn.digital.shipment.domain.models.ShipmentItem items1 = new ShipmentItem("1", "item1", 1);
            com.atn.digital.shipment.domain.models.ShipmentItem items2 = new ShipmentItem("2", "item2", 2);
            shipment.getItems().add(items1);
            shipment.getItems().add(items2);
        }
    }

    @Nested
    class OnUpdateStatus {
        @Test
        void statusUnchangedWhenNewValueIsNull() {
            Shipment shipment = Shipment.withoutId("shipmentId", "deliveryAddress");
            shipment.updateStatus(null, "reason");
            Assertions.assertEquals(ShipmentStatus.CREATED, shipment.getStatus());
        }

        @Test
        void statusUnchangedWhenNewValueIsIdenticalToOld() {
            Shipment shipment = Shipment.withoutId("shipmentId", "deliveryAddress");
            shipment.updateStatus(ShipmentStatus.CREATED, "reason");
            Assertions.assertEquals(ShipmentStatus.CREATED, shipment.getStatus());
            Assertions.assertEquals(1, shipment.getStatusChangeHistory().size());
        }

        @Test
        void statusUnchangedWhenNewValueHasInferiorPriority() {
            Shipment shipment = Shipment.withoutId("shipmentId", "deliveryAddress");
            shipment.updateStatus(ShipmentStatus.UNKNOWN, "reason");
            Assertions.assertEquals(ShipmentStatus.CREATED, shipment.getStatus());
            Assertions.assertEquals(1, shipment.getStatusChangeHistory().size());
        }

        @Test
        void statusIsEqualsToNewValue() {
            Shipment shipment = Shipment.withoutId("shipmentId", "deliveryAddress");
            shipment.updateStatus(ShipmentStatus.ON_THE_WAY, "reason");
            Assertions.assertEquals(ShipmentStatus.ON_THE_WAY, shipment.getStatus());
            Assertions.assertEquals(2, shipment.getStatusChangeHistory().size());
            Assertions.assertEquals(ShipmentStatus.ON_THE_WAY,
                    shipment.getStatusChangeHistory().get(1).newStatus());
        }
    }
}
