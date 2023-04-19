package com.atn.digital.user.adapters.in.rabbitmq;

import java.util.List;

public record OrderStatusChangedEvent(
        String orderId,
        String userId,
        String deliveryAddress,
        List<OrderItemData> items,
        String oldStatus,
        String newStatus,
        String reason) { }
