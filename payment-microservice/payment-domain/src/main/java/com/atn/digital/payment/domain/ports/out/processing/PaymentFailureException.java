package com.atn.digital.payment.domain.ports.out.processing;


public class PaymentFailureException extends RuntimeException {

    public PaymentFailureException(String message) {
        super(message);
    }

    public PaymentFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
