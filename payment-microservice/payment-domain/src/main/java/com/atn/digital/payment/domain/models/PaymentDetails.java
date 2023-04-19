package com.atn.digital.payment.domain.models;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentDetails(String id, String customerId, String orderId, BigDecimal amount, String reason,
                             Instant instant, PaymentStatus status) { }
