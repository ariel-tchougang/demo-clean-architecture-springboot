package com.atn.digital.shipment.domain.ports.in.usecases;

import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;

public interface CreateNewShipmentUseCase {
    ShipmentId handle(CreateNewShipmentCommand newShipmentCommand);
}
