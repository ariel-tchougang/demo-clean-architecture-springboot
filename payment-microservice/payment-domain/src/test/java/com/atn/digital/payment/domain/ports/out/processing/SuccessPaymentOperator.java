package com.atn.digital.payment.domain.ports.out.processing;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.models.PaymentStatus;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;

public class SuccessPaymentOperator implements PaymentOperator {

    public PaymentDetails processPayment(ProcessPaymentCommand processPaymentCommand) {
        return new PaymentDetails(
                null,
                processPaymentCommand.customerId(),
                processPaymentCommand.orderId(),
                processPaymentCommand.amount(),
                processPaymentCommand.reason(),
                null,
                PaymentStatus.COMPLETED);
    }
}
