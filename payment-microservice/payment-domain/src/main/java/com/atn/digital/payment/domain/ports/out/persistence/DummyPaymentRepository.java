package com.atn.digital.payment.domain.ports.out.persistence;

import com.atn.digital.payment.domain.models.PaymentDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DummyPaymentRepository  implements SavePaymentDetailsPort, LoadPaymentDetailsPort {

    private final List<PaymentDetails> payments = new ArrayList<>();

    public String save(PaymentDetails paymentDetails) {
        PaymentDetails details = new PaymentDetails(
                UUID.randomUUID().toString(),
                paymentDetails.customerId(),
                paymentDetails.orderId(),
                paymentDetails.amount(),
                paymentDetails.reason(),
                Instant.now(),
                paymentDetails.status()
        );
        payments.add(details);
        return details.id();
    }

    public Optional<PaymentDetails> findPaymentDetailsById(String id) {
        return payments.stream().filter(item -> id.equals(item.id())).findFirst();
    }

    public List<PaymentDetails> payments() { return payments; }
}
