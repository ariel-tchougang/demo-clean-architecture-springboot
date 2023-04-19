package com.atn.digital.payment.domain.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PaymentStatusTest {

    @Test
    void getStatusShouldReturnCompletedWhenValueIsCompleted() {
        Assertions.assertEquals(PaymentStatus.COMPLETED, PaymentStatus.getStatus("COMPLETED"));
    }

    @Test
    void getStatusShouldReturnFailedWhenValueIsFailed() {
        Assertions.assertEquals(PaymentStatus.FAILED, PaymentStatus.getStatus("FAILED"));
    }

    @Test
    void getStatusShouldReturnUnknownWhenValueIsNeitherCompletedNorFailed() {
        Assertions.assertEquals(PaymentStatus.UNKNOWN, PaymentStatus.getStatus("baDabouM"));
    }

    @Test
    void getStatusShouldBeNonCaseSensitive() {
        Assertions.assertEquals(PaymentStatus.FAILED, PaymentStatus.getStatus("fAIleD"));
        Assertions.assertEquals(PaymentStatus.COMPLETED, PaymentStatus.getStatus("CoMpleted"));
    }
}
