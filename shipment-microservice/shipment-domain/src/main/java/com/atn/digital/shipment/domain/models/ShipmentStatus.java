package com.atn.digital.shipment.domain.models;

import java.util.Arrays;
import java.util.List;

public enum ShipmentStatus {
    CREATED(1),
    ON_THE_WAY(2),
    DELIVERED(3),
    LOST(4),
    UNKNOWN(0);

    private final int priority;
    private ShipmentStatus(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public static ShipmentStatus getStatus(String value) {
        if (value == null) {
            return UNKNOWN;
        }

        List<ShipmentStatus> values = Arrays.stream(ShipmentStatus.values()).toList();
        List<String> names = values.stream().map(status -> status.name()).toList();
        int index = names.indexOf(value.toUpperCase());

        if (index < 0) {
            return UNKNOWN;
        }

        return values.get(index);
    }
}
