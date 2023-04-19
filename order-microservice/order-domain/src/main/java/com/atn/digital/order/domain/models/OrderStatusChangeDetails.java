package com.atn.digital.order.domain.models;

import java.time.Instant;

public record OrderStatusChangeDetails(OrderStatus oldStatus, OrderStatus newStatus, Instant changeDate,
                                       String reason) { }
