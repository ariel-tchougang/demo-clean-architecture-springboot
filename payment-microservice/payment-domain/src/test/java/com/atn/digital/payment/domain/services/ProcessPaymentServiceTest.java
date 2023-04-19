package com.atn.digital.payment.domain.services;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.models.PaymentStatus;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;
import com.atn.digital.payment.domain.ports.out.notifications.DummyPaymentPublisher;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentCompletedEvent;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentFailedEvent;
import com.atn.digital.payment.domain.ports.out.persistence.DummyPaymentRepository;
import com.atn.digital.payment.domain.ports.out.processing.FailurePaymentOperator;
import com.atn.digital.payment.domain.ports.out.processing.PaymentFailureException;
import com.atn.digital.payment.domain.ports.out.processing.SuccessPaymentOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class ProcessPaymentServiceTest {

    private final SuccessPaymentOperator successPaymentHandler = new SuccessPaymentOperator();
    private final FailurePaymentOperator failurePaymentHandler = new FailurePaymentOperator();
    private final DummyPaymentRepository repository = new DummyPaymentRepository();
    private final DummyPaymentPublisher publisher = new DummyPaymentPublisher();
    private final ProcessPaymentService processPaymentSuccess = new ProcessPaymentService(successPaymentHandler, repository, publisher, publisher);
    private final ProcessPaymentService processPaymentFailure = new ProcessPaymentService(failurePaymentHandler, repository, publisher, publisher);
    private final String someCustomerId = "ce71c593-d51a-400f-93ab-01348aba0b4d";
    private final String someOrderId = "b1f6d93b-4ef6-4c19-a810-edbe7291dc51";

    @Nested
    class OnPaymentSuccess {

        @Test
        void shouldExistPaymentDetailsWithStatusCompleted() {
            ProcessPaymentCommand command = new ProcessPaymentCommand(
                    someOrderId,
                    someCustomerId,
                    "Testing",
                    BigDecimal.TEN
            );

            processPaymentSuccess.handle(command);
            PaymentDetails dbPayment = repository.payments().get(0);
            PaymentDetails details = new PaymentDetails(
                    dbPayment.id(),
                    command.customerId(),
                    command.orderId(),
                    command.amount(),
                    command.reason(),
                    dbPayment.instant(),
                    PaymentStatus.COMPLETED);
            Assertions.assertEquals(dbPayment, details);
        }

        @Test
        void shouldPublishPaymentSuccessEvent() {
            ProcessPaymentCommand command = new ProcessPaymentCommand(
                    someOrderId,
                    someCustomerId,
                    "Testing",
                    BigDecimal.TEN
            );

            processPaymentSuccess.handle(command);
            PaymentDetails dbPayment = repository.payments().get(0);
            PaymentCompletedEvent event = new PaymentCompletedEvent(dbPayment.id(), command.orderId());
            assertExistingCompletedEvents(event);
        }
    }

    @Nested
    class OnPaymentFailure {

        @Test
        void shouldThrowPaymentFailureException() {
            ProcessPaymentCommand command = new ProcessPaymentCommand(
                    someOrderId,
                    someCustomerId,
                    "Testing",
                    BigDecimal.TEN
            );

            Assertions.assertThrows(PaymentFailureException.class, () -> processPaymentFailure.handle(command));
        }

        @Test
        void shouldExistPaymentDetailsWithStatusFailed() {
            ProcessPaymentCommand command = new ProcessPaymentCommand(
                    someOrderId,
                    someCustomerId,
                    "Testing",
                    BigDecimal.TEN
            );

            Assertions.assertThrows(PaymentFailureException.class, () -> processPaymentFailure.handle(command));

            PaymentDetails dbPayment = repository.payments().get(0);

            PaymentDetails details = new PaymentDetails(
                    dbPayment.id(),
                    command.customerId(),
                    command.orderId(),
                    command.amount(),
                    command.reason(),
                    dbPayment.instant(),
                    PaymentStatus.FAILED);
            Assertions.assertEquals(dbPayment, details);
        }

        @Test
        void shouldPublishPaymentFailureEvent() {
            ProcessPaymentCommand command = new ProcessPaymentCommand(
                    someOrderId,
                    someCustomerId,
                    "Testing",
                    BigDecimal.TEN
            );

            Assertions.assertThrows(PaymentFailureException.class, () -> processPaymentFailure.handle(command));
            PaymentDetails dbPayment = repository.payments().get(0);
            RuntimeException exception = new RuntimeException("An exception occurred when processing payment");
            PaymentFailedEvent event = new PaymentFailedEvent(
                    dbPayment.id(),
                    command.orderId(),
                    exception.getMessage());
            assertExistingFailedEvents(event);
        }
    }

    private void assertExistingCompletedEvents(PaymentCompletedEvent... eventsContained) {
        assertThat(publisher.completedEvents()).containsExactly(eventsContained);
    }

    private void assertExistingFailedEvents(PaymentFailedEvent... eventsContained) {
        assertThat(publisher.failedEvents()).containsExactly(eventsContained);
    }
}
