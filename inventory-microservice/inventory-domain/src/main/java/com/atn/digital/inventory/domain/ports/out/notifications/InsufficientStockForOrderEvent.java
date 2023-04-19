package com.atn.digital.inventory.domain.ports.out.notifications;

import com.atn.digital.inventory.domain.models.OrderItemData;

import java.util.List;
public record InsufficientStockForOrderEvent(String orderId, List<OrderItemData> items) { }
