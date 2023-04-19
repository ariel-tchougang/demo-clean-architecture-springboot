package com.atn.digital.payment.domain.ports.out.notifications;

public record PaymentFailedEvent(String paymentId, String orderId, String errorMessage) {}
