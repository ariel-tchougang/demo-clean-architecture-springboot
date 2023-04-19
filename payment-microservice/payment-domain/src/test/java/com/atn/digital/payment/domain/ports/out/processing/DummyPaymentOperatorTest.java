package com.atn.digital.payment.domain.ports.out.processing;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.models.PaymentStatus;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class DummyPaymentOperatorTest {

    private final DummyPaymentOperator paymentOperator = new DummyPaymentOperator();

    @Test
    void shouldTrowPaymentFailureExceptionWhenCommandIsNull() {
        Assertions.assertThrows(PaymentFailureException.class, () -> paymentOperator.processPayment(null));
    }

    @Test
    void shouldTrowPaymentFailureExceptionWhenAmountIsNull() {
        ProcessPaymentCommand command = new ProcessPaymentCommand(null, null, null, null);
        Assertions.assertThrows(PaymentFailureException.class, () -> paymentOperator.processPayment(command));
    }

    @Test
    void shouldTrowPaymentFailureExceptionWhenAmountIsNegative() {
        ProcessPaymentCommand command = new ProcessPaymentCommand(null, null, null, BigDecimal.TEN.negate());
        Assertions.assertThrows(PaymentFailureException.class, () -> paymentOperator.processPayment(command));
    }

    @Test
    void shouldReturnPaymentDetailsWithStatusCompleted() {
        ProcessPaymentCommand command = new ProcessPaymentCommand(null, null, null, BigDecimal.TEN);
        PaymentDetails details = paymentOperator.processPayment(command);
        Assertions.assertEquals(PaymentStatus.COMPLETED, details.status());
        Assertions.assertEquals(command.orderId(), details.orderId());
        Assertions.assertEquals(command.customerId(), details.customerId());
        Assertions.assertEquals(command.reason(), details.reason());
        Assertions.assertEquals(command.amount(), details.amount());
    }
}
