package com.atn.digital.shipment.domain.ports.out.notifications;

import java.util.ArrayList;
import java.util.List;

public class InMemoryShipmentEventPublisher
        implements ShipmentCreatedEventPublisherPort, ShipmentStatusChangedEventPublisherPort {

    private final List<ShipmentCreatedEvent> createdShipmentEvents = new ArrayList<>();
    private final List<ShipmentStatusChangedEvent> statusChangedShipmentEvents = new ArrayList<>();

    public void publish(ShipmentCreatedEvent event) {
        createdShipmentEvents.add(event);
        System.out.printf("ShipmentCreatedEvent [id=%s]%n", event.shipmentId());
    }

    public void publish(ShipmentStatusChangedEvent event) {
        statusChangedShipmentEvents.add(event);
        System.out.printf("ShipmentStatusChangedEvent [id=%s, oldStatus=%s, newStatus=%s]%n",
                event.orderId(),
                event.oldStatus(),
                event.newStatus());
    }

    public List<ShipmentCreatedEvent> createdShipmentEvents() {
        return createdShipmentEvents;
    }

    public List<ShipmentStatusChangedEvent> statusChangedShipmentEvents() {
        return statusChangedShipmentEvents;
    }
}
