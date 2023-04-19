package com.atn.digital.payment.domain.ports.out.persistence;

import com.atn.digital.payment.domain.models.PaymentDetails;

import java.util.List;
import java.util.Optional;

public interface LoadPaymentDetailsPort {
    Optional<PaymentDetails> findPaymentDetailsById(String id);
}
