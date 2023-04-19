package com.atn.digital.payment.domain.ports.out.notifications;

public record PaymentCompletedEvent(String paymentId, String orderId) {}
