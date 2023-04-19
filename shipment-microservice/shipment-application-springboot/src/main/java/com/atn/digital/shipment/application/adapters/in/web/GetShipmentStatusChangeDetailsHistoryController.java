package com.atn.digital.shipment.application.adapters.in.web;

import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentStatusChangeDetails;
import com.atn.digital.shipment.domain.ports.in.queries.GetShipmentStatusChangeDetailsHistoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetShipmentStatusChangeDetailsHistoryController {

    private final GetShipmentStatusChangeDetailsHistoryQuery query;

    @GetMapping("/api/v1/shipments/status/history/{shipmentId}")
    public ResponseEntity<List<ShipmentStatusChangeDetails>> getStatusChangeDetailsHistory(
            @PathVariable("shipmentId") String shipmentId) {
        List<ShipmentStatusChangeDetails> history = query.getStatusChangeDetailsHistory(new ShipmentId(shipmentId));
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
