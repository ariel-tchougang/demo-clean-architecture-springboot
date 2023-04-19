package com.atn.digital.shipment.domain.ports.out.persistence;

import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;

public interface CreateNewShipmentPort {
    ShipmentId createNewShipment(Shipment newShipment);
}
