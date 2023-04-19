package com.atn.digital.shipment.domain.ports.out.notifications;

public record ShipmentCreatedEvent(String shipmentId, String orderId) { }
