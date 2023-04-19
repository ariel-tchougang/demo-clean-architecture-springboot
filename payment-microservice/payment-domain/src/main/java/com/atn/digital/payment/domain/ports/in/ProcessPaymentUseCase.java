package com.atn.digital.payment.domain.ports.in;

import com.atn.digital.payment.domain.ports.out.processing.PaymentFailureException;

import java.math.BigDecimal;

public interface ProcessPaymentUseCase {
    void handle(ProcessPaymentCommand processPaymentCommand) throws PaymentFailureException;
}
