package com.atn.digital.shipment.domain.ports.out.persistence;

import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentItem;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FakeLoadShipmentPort implements LoadShipmentPort {
    public Optional<Shipment> loadShipment(ShipmentId shipmentId) {
        List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
        Shipment order = Shipment.withId(shipmentId, "orderId", "deliveryAddress");
        order.getItems().addAll(items);
        return Optional.of(order);
    }
}
