package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.domain.ports.out.notifications.OrderItemData;

import java.util.List;
public record InsufficientStockForOrderEvent(String orderId, List<OrderItemData> items) { }
