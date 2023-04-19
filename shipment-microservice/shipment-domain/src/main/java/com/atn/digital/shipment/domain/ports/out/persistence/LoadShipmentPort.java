package com.atn.digital.shipment.domain.ports.out.persistence;

import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;

import java.util.Optional;

public interface LoadShipmentPort {
    Optional<Shipment> loadShipment(ShipmentId shipmentId);
}
