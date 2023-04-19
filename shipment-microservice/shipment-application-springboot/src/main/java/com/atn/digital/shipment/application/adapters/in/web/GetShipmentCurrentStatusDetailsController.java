package com.atn.digital.shipment.application.adapters.in.web;

import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;
import com.atn.digital.shipment.domain.ports.in.queries.GetShipmentCurrentStatusDetailsQuery;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class GetShipmentCurrentStatusDetailsController {

    private final GetShipmentCurrentStatusDetailsQuery query;

    @GetMapping("/api/v1/shipments/status/{shipmentId}")
    public ResponseEntity<ShipmentStatusDetails> getStatusDetails(
            @PathVariable("shipmentId") @NotNull @NotEmpty String shipmentId) {
        Optional<ShipmentStatusDetails> details = query.getStatusDetails(new ShipmentId(shipmentId));
        return ResponseEntity.of(details);
    }
}
