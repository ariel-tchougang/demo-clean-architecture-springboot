package com.atn.digital.payment.application.adapters.out.notifications;

import com.atn.digital.payment.domain.ports.out.notifications.PublishPaymentCompletedPort;
import com.atn.digital.payment.domain.ports.out.notifications.PublishPaymentFailedPort;

public abstract class PaymentEventPublisherAdapter implements PublishPaymentCompletedPort, PublishPaymentFailedPort { }
