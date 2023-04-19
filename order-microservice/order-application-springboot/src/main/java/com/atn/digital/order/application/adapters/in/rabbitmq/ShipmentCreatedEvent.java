package com.atn.digital.order.application.adapters.in.rabbitmq;

public record ShipmentCreatedEvent(String shipmentId, String orderId) { }
