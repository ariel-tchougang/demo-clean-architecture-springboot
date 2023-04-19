package com.atn.digital.shipment.domain.services;

import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentUseCase;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentCreatedEvent;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentCreatedEventPublisherPort;
import com.atn.digital.shipment.domain.ports.out.persistence.CreateNewShipmentPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreateNewShipmentService implements CreateNewShipmentUseCase {

    private final CreateNewShipmentPort newShipmentPort;
    private final ShipmentCreatedEventPublisherPort shipmentEventPublisher;

    public ShipmentId handle(CreateNewShipmentCommand newShipmentCommand) {

        if (newShipmentCommand == null) {
            throw new IllegalArgumentException("newShipmentCommand must not be null!");
        }

        List<ShipmentItem> items = newShipmentCommand.getItems().stream()
                .map(item -> new ShipmentItem(item.getId(), item.getName(), item.getQuantity()))
                .toList();

        Shipment newShipment = Shipment.withoutId(
                newShipmentCommand.getOrderId(),
                newShipmentCommand.getDeliveryAddress());
        newShipment.getItems().addAll(items);

        ShipmentId shipmentId = newShipmentPort.createNewShipment(newShipment);

        shipmentEventPublisher.publish(new ShipmentCreatedEvent(shipmentId.getId(), newShipment.getOrderId()));

        return shipmentId;
    }
}
