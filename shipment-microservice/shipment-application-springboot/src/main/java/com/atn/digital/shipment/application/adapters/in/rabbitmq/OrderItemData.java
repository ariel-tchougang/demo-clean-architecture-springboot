package com.atn.digital.shipment.application.adapters.in.rabbitmq;

import java.math.BigDecimal;

public record OrderItemData(String id, String name, Integer quantity, BigDecimal unitaryPrice) { }
