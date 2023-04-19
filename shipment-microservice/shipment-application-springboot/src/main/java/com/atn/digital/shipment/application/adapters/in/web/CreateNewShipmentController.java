package com.atn.digital.shipment.application.adapters.in.web;

import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentUseCase;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateNewShipmentController {

    private final CreateNewShipmentUseCase useCase;

    @PostMapping("/api/v1/shipments")
    public ResponseEntity<ShipmentIdDto> createNewShipment(
            @RequestBody @NotNull CreateNewShipmentWeb newShipmentWeb) {
        CreateNewShipmentCommand newUserCommand = new CreateNewShipmentCommand(
                newShipmentWeb.getOrderId(),
                newShipmentWeb.getDeliveryAddress(),
                newShipmentWeb.getItems().stream().map(item -> new ShipmentItem(
                        item.id(),
                        item.name(),
                        item.quantity())
                ).toList());
        ShipmentId shipmentId = useCase.handle(newUserCommand);
        return new ResponseEntity<>(new ShipmentIdDto(shipmentId.getId()), HttpStatus.CREATED);
    }
}
