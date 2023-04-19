package com.atn.digital.payment.application.adapters.in.rabbitmq;

import java.util.List;

public record OrderStatusChangedEvent(
        String orderId,
        String customerId,
        String deliveryAddress,
        List<OrderItemData> items,
        String oldStatus,
        String newStatus,
        String reason) { }
