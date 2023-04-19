package com.atn.digital.shipment.domain.ports.out.notifications;

public interface ShipmentStatusChangedEventPublisherPort {
    void publish(ShipmentStatusChangedEvent event);
}
