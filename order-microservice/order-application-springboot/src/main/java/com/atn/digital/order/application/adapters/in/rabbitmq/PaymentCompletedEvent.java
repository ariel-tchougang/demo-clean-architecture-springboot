package com.atn.digital.order.application.adapters.in.rabbitmq;

public record PaymentCompletedEvent(String paymentId, String orderId) {}
