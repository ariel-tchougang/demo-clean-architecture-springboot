package com.atn.digital.shipment.domain.ports.in.usecases;

import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;

public interface UpdateShipmentStatusUseCase {
    ShipmentStatusDetails handle(UpdateShipmentStatusCommand updateOrderStatusCommand);
}
