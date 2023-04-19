package com.atn.digital.shipment.domain.ports.in.queries;

import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentStatusChangeDetails;

import java.util.List;

public interface GetShipmentStatusChangeDetailsHistoryQuery {
    List<ShipmentStatusChangeDetails> getStatusChangeDetailsHistory(ShipmentId shipmentId);
}
