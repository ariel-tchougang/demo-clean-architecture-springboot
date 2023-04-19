package com.atn.digital.shipment.domain.services;

import com.atn.digital.shipment.domain.exceptions.ShipmentNotFoundException;
import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;
import com.atn.digital.shipment.domain.ports.in.queries.GetShipmentCurrentStatusDetailsQuery;
import com.atn.digital.shipment.domain.ports.out.persistence.LoadShipmentPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GetShipmentCurrentStatusDetailsService implements GetShipmentCurrentStatusDetailsQuery {

    private final LoadShipmentPort port;

    public Optional<ShipmentStatusDetails> getStatusDetails(ShipmentId shipmentId) {

        if (shipmentId == null) {
            throw new IllegalArgumentException("shipmentId must not be null!");
        }

        Optional<Shipment> optional = port.loadShipment(shipmentId);

        if (optional.isEmpty()) {
            throw new ShipmentNotFoundException("shipmentId not found: " + shipmentId);
        }

        return Optional.of(optional.get().getStatusDetails());
    }
}
