package com.atn.digital.shipment.domain.ports.out.notifications;

public interface ShipmentCreatedEventPublisherPort {
    void publish(ShipmentCreatedEvent event);
}
