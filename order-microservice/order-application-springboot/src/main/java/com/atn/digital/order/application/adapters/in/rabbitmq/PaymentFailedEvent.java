package com.atn.digital.order.application.adapters.in.rabbitmq;

public record PaymentFailedEvent(String paymentId, String orderId, String errorMessage) {}
