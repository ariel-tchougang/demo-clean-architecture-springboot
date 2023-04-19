package com.atn.digital.shipment.domain.ports.in.queries;

import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;

import java.util.Optional;

public interface GetShipmentCurrentStatusDetailsQuery {
    Optional<ShipmentStatusDetails> getStatusDetails(ShipmentId shipmentId);
}
