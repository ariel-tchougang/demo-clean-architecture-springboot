package com.atn.digital.payment.domain.ports.out.processing;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;

public interface PaymentOperator {

    PaymentDetails processPayment(ProcessPaymentCommand processPaymentCommand) throws PaymentFailureException;
}
