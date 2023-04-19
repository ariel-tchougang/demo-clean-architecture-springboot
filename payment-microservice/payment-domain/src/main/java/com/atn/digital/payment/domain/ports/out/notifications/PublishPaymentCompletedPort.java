package com.atn.digital.payment.domain.ports.out.notifications;

public interface PublishPaymentCompletedPort {
    void publish(PaymentCompletedEvent event);
}
