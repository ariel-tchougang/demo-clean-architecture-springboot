package com.atn.digital.user.domain.models;

import java.math.BigDecimal;
import java.util.List;

public record Order(
        String id,
        String userId,
        String deliveryAddress,
        List<OrderItem> items,
        BigDecimal amount,
        String status,
        String message) { }
