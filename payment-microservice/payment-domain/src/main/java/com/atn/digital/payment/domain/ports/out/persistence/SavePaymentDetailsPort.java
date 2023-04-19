package com.atn.digital.payment.domain.ports.out.persistence;

import com.atn.digital.payment.domain.models.PaymentDetails;

public interface SavePaymentDetailsPort {
    String save(PaymentDetails paymentDetails);
}
