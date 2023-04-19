package com.atn.digital.payment.domain.ports.out.persistence;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.models.PaymentStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

class DummyPaymentRepositoryTest {

    private final DummyPaymentRepository repository = new DummyPaymentRepository();

    @Test
    void shouldExistNewPaymentDetailsInRepositoryWhenSaveIsSuccessful() {
        String customerId = UUID.randomUUID().toString();
        PaymentStatus status = PaymentStatus.UNKNOWN;
        String orderId = UUID.randomUUID().toString();
        PaymentDetails details = new PaymentDetails(
                null,
                customerId,
                orderId,
                null,
                null,
                Instant.now(),
                status);

        String paymentId = repository.save(details);

        Optional<PaymentDetails> optional = repository.findPaymentDetailsById(paymentId);
        Assertions.assertTrue(optional.isPresent());
        details = optional.get();
        Assertions.assertEquals(customerId, details.customerId());
        Assertions.assertEquals(paymentId, details.id());
        Assertions.assertEquals(status, details.status());
    }
}
