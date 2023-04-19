package com.atn.digital.order.domain.ports.out.notifications;

import java.math.BigDecimal;

public record OrderItemData(String id, String name, Integer quantity, BigDecimal unitaryPrice) { }
