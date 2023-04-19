package com.atn.digital.inventory.domain.models;

import java.math.BigDecimal;

public record OrderItemData(String id, String name, Integer quantity, BigDecimal unitaryPrice) { }
