package com.atn.digital.order.domain.models;

import java.util.Arrays;
import java.util.List;

public enum OrderStatus {
    CREATED(1),
    INVENTORY_CHECK_OK(2),
    PAYMENT_COMPLETED(3),
    PROCESSING_IN_PROGRESS(4),
    PENDING_DELIVERY(5),
    DELIVERED(6),
    CANCELLED(6),
    UNKNOWN(0);

    private final int priority;
    private OrderStatus(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static OrderStatus getStatus(String value) {
        if (value == null) {
            return UNKNOWN;
        }

        List<OrderStatus> values = Arrays.stream(OrderStatus.values()).toList();
        List<String> names = values.stream().map(Enum::name).toList();
        int index = names.indexOf(value.toUpperCase());

        if (index < 0) {
            return UNKNOWN;
        }

        return values.get(index);
    }
}
