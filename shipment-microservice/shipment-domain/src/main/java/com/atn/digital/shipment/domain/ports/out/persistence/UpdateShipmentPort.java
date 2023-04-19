package com.atn.digital.shipment.domain.ports.out.persistence;

import com.atn.digital.shipment.domain.models.Shipment;

public interface UpdateShipmentPort {
    Shipment update(Shipment shipment);
}
