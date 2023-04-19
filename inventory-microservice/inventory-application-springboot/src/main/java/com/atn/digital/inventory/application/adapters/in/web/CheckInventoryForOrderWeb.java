package com.atn.digital.inventory.application.adapters.in.web;

import com.atn.digital.inventory.domain.models.OrderItemData;

import java.util.List;

public record CheckInventoryForOrderWeb(String orderId, List<OrderItemData> items) { }
