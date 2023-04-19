package com.atn.digital.shipment.domain.ports.out.persistence;

import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;

import java.util.UUID;

public class DummyCreateNewShipmentPort implements CreateNewShipmentPort {
    public ShipmentId createNewShipment(Shipment newShipment) {
        Shipment dbShipment = Shipment.withId(
                new ShipmentId(UUID.randomUUID().toString()),
                newShipment.getOrderId(),
                newShipment.getDeliveryAddress()
        );
        dbShipment.getItems().addAll(newShipment.getItems());
        return dbShipment.getId().get();
    }
}
