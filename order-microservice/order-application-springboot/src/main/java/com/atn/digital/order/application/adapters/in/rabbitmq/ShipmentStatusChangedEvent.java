package com.atn.digital.order.application.adapters.in.rabbitmq;

public record ShipmentStatusChangedEvent(String shipmentId, String orderId, String oldStatus, String newStatus) { }
