package com.atn.digital.shipment.domain.ports.out.persistence;

import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;

import java.util.Optional;

public class FailedLoadShipmentPort implements LoadShipmentPort {
    public Optional<Shipment> loadShipment(ShipmentId shipmentId) {
        return Optional.empty();
    }
}
