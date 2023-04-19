package com.atn.digital.order.domain.ports.out.notifications;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEvent(
        String orderId,
        String userId,
        String deliveryAddress,
        List<OrderItemData> items,
        BigDecimal amount) { }
