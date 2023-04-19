package com.atn.digital.payment.domain.services;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.models.PaymentStatus;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentUseCase;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentCompletedEvent;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentFailedEvent;
import com.atn.digital.payment.domain.ports.out.notifications.PublishPaymentCompletedPort;
import com.atn.digital.payment.domain.ports.out.notifications.PublishPaymentFailedPort;
import com.atn.digital.payment.domain.ports.out.persistence.SavePaymentDetailsPort;
import com.atn.digital.payment.domain.ports.out.processing.PaymentFailureException;
import com.atn.digital.payment.domain.ports.out.processing.PaymentOperator;

import java.time.Instant;

public class ProcessPaymentService implements ProcessPaymentUseCase {

    private final PaymentOperator operator;
    private final SavePaymentDetailsPort repository;
    private final PublishPaymentCompletedPort successPublisher;
    private final PublishPaymentFailedPort failurePublisher;

    public ProcessPaymentService(PaymentOperator operator, SavePaymentDetailsPort repository,
                                 PublishPaymentCompletedPort successPublisher,
                                 PublishPaymentFailedPort failurePublisher) {
        this.operator = operator;
        this.repository = repository;
        this.successPublisher = successPublisher;
        this.failurePublisher = failurePublisher;
    }

    public void handle(ProcessPaymentCommand processPaymentCommand) throws PaymentFailureException {
        PaymentDetails paymentDetails = null;

        try {
            paymentDetails = operator.processPayment(processPaymentCommand);
            String paymentId = repository.save(paymentDetails);
            successPublisher.publish(new PaymentCompletedEvent(paymentId, paymentDetails.orderId()));
        } catch (Exception exception) {
            paymentDetails = new PaymentDetails(
                    null,
                    processPaymentCommand.customerId(),
                    processPaymentCommand.orderId(),
                    processPaymentCommand.amount(),
                    processPaymentCommand.reason(),
                    Instant.now(),
                    PaymentStatus.FAILED);

            String paymentId = repository.save(paymentDetails);

            failurePublisher.publish(new PaymentFailedEvent(
                    paymentId,
                    paymentDetails.orderId(),
                    exception.getMessage()
            ));

            throw (exception instanceof PaymentFailureException) ? (PaymentFailureException)exception :
                    new PaymentFailureException(exception.getMessage(), exception);
        }
    }
}
