package com.atn.digital.order.domain.ports.out.notifications;

import java.util.List;

public record OrderStatusChangedEvent(
        String orderId,
        String customerId,
        String deliveryAddress,
        List<OrderItemData> items,
        String oldStatus,
        String newStatus,
        String reason) { }
