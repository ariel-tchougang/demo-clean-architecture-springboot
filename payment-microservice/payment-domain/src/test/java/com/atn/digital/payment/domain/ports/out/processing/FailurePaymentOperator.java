package com.atn.digital.payment.domain.ports.out.processing;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;

public class FailurePaymentOperator implements PaymentOperator {

    public PaymentDetails processPayment(ProcessPaymentCommand processPaymentCommand) {
        throw new RuntimeException("An exception occurred when processing payment");
    }
}
