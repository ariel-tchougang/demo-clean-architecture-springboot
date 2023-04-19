package com.atn.digital.user.adapters.in.rabbitmq;

import java.math.BigDecimal;

public record OrderItemData(String id, String name, Integer quantity, BigDecimal unitaryPrice) { }
