package com.atn.digital.shipment.domain.ports.out.persistence;

import com.atn.digital.shipment.domain.models.Shipment;

public class FakeUpdateShipmentPort implements UpdateShipmentPort {
    public Shipment update(Shipment shipment) {
        return shipment;
    }
}
