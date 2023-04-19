package com.atn.digital.shipment.domain.services;

import com.atn.digital.shipment.domain.exceptions.ShipmentNotFoundException;
import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentStatus;
import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusUseCase;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentStatusChangedEvent;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentStatusChangedEventPublisherPort;
import com.atn.digital.shipment.domain.ports.out.persistence.LoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.UpdateShipmentPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UpdateShipmentStatusService implements UpdateShipmentStatusUseCase {

    private final LoadShipmentPort loadShipmentPort;
    private final UpdateShipmentPort updateShipmentPort;
    private final ShipmentStatusChangedEventPublisherPort publisher;

    public ShipmentStatusDetails handle(UpdateShipmentStatusCommand updateOrderStatusCommand) {

        if (updateOrderStatusCommand == null) {
            throw new IllegalArgumentException("updateOrderStatusCommand must not be null!");
        }

        Optional<Shipment> optional = loadShipmentPort.loadShipment(
                new ShipmentId(updateOrderStatusCommand.getShipmentId()));

        if (optional.isEmpty()) {
            throw new ShipmentNotFoundException("shipmentId not found: " + updateOrderStatusCommand.getShipmentId());
        }

        Shipment shipment = optional.get();
        ShipmentStatus oldStatus = shipment.getStatus();

        shipment.updateStatus(updateOrderStatusCommand.getNewStatus(), updateOrderStatusCommand.getReason());

        ShipmentStatusDetails statusDetails = updateShipmentPort.update(shipment).getStatusDetails();

        if (statusDetails.status() != oldStatus) {
            publisher.publish(
                    new ShipmentStatusChangedEvent(
                            shipment.getId().get().getId(),
                            shipment.getOrderId(),
                            oldStatus.name(),
                            statusDetails.status().name()
                    )
            );
        }

        return statusDetails;
    }
}
