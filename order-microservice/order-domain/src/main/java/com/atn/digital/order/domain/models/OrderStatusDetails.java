package com.atn.digital.order.domain.models;

import java.time.Instant;

public record OrderStatusDetails (OrderStatus status, Instant changeDate, String reason) { }
