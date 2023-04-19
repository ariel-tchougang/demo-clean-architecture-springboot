package com.atn.digital.payment.domain.ports.in;

import java.math.BigDecimal;

public record ProcessPaymentCommand(String orderId, String customerId, String reason, BigDecimal amount){ }
