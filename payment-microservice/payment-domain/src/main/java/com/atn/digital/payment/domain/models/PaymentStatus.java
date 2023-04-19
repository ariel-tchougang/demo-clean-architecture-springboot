package com.atn.digital.payment.domain.models;

public enum PaymentStatus {
    COMPLETED,
    FAILED,
    UNKNOWN;

    public static PaymentStatus getStatus(String value) {
        if (COMPLETED.name().equalsIgnoreCase(value)) {
            return COMPLETED;
        }

        if (FAILED.name().equalsIgnoreCase(value)) {
            return FAILED;
        }

        return UNKNOWN;
    }
}
