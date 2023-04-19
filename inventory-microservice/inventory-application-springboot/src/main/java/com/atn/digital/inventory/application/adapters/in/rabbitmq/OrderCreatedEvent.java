package com.atn.digital.inventory.application.adapters.in.rabbitmq;

import com.atn.digital.inventory.domain.models.OrderItemData;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEvent (String orderId, String customerId, String deliveryAddress,
                                 List<OrderItemData> items, BigDecimal amount) { }
