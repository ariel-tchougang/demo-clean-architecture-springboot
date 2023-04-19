package com.atn.digital.shipment.application.adapters.in.web;

import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UpdateShipmentStatusController {

    private final UpdateShipmentStatusUseCase useCase;

    @PostMapping("/api/v1/shipments/status")
    public ResponseEntity<ShipmentStatusDetails> updateShipmentStatus(
            @RequestBody UpdateShipmentStatusWeb shipmentStatusWeb) {
        UpdateShipmentStatusCommand updateShipmentStatusCommand = new UpdateShipmentStatusCommand(
                shipmentStatusWeb.getShipmentId(),
                shipmentStatusWeb.getNewStatus(),
                shipmentStatusWeb.getReason());
        ShipmentStatusDetails details = useCase.handle(updateShipmentStatusCommand);
        return new ResponseEntity<>(details, HttpStatus.OK);
    }
}
