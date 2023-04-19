package com.atn.digital.shipment.domain.ports.out.notifications;

public record ShipmentStatusChangedEvent(String shipmentId, String orderId, String oldStatus, String newStatus) { }
