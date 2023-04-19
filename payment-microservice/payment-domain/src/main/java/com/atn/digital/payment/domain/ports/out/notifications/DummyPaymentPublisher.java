package com.atn.digital.payment.domain.ports.out.notifications;

import java.util.ArrayList;
import java.util.List;

public class DummyPaymentPublisher implements PublishPaymentCompletedPort, PublishPaymentFailedPort{

    private final List<PaymentCompletedEvent> completedPayments = new ArrayList<>();
    private final List<PaymentFailedEvent> failedPayments = new ArrayList<>();

    public void publish(PaymentCompletedEvent event) {
        completedPayments.add(event);
        System.out.println("PaymentCompletedEvent: " + event.paymentId());
    }

    public void publish(PaymentFailedEvent event) {
        failedPayments.add(event);
        System.err.println("PaymentFailedEvent: " + event.paymentId());
    }

    public List<PaymentCompletedEvent> completedEvents() {
        return completedPayments;
    }

    public List<PaymentFailedEvent> failedEvents() {
        return failedPayments;
    }
}
