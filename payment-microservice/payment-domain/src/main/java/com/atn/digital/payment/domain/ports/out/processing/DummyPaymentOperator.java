package com.atn.digital.payment.domain.ports.out.processing;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.models.PaymentStatus;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;

import java.time.Instant;

public class DummyPaymentOperator implements PaymentOperator {

    public PaymentDetails processPayment(ProcessPaymentCommand processPaymentCommand) throws PaymentFailureException {

        if (processPaymentCommand == null) {
            throw new PaymentFailureException("Payload is null");
        }

        if (processPaymentCommand.amount() == null || processPaymentCommand.amount().doubleValue() < 0) {
            throw new PaymentFailureException("Invalid amount");
        }

        if (processPaymentCommand.amount().doubleValue() > 100) {
            throw new PaymentFailureException("Amount is greater than maximum limit authorized");
        }

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
