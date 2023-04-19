package com.atn.digital.payment.domain.ports.out.notifications;

public interface PublishPaymentFailedPort {
    void publish(PaymentFailedEvent event);
}
