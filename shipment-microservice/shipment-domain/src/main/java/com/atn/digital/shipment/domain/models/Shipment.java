package com.atn.digital.shipment.domain.models;

import com.atn.digital.commons.validation.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Shipment {

    private final ShipmentId id;
    private final String orderId;
    private final String deliveryAddress;

    private ShipmentStatus status;
    private Instant creationDate;

    private final List<ShipmentItem> items = new ArrayList<>();
    private final List<ShipmentStatusChangeDetails> statusChangeHistory = new ArrayList<>();

    private Shipment(ShipmentId id, String customerId, String deliveryAddress) {
        this.id = id;
        this.orderId = customerId;
        this.deliveryAddress = deliveryAddress;
        init();
    }

    public static Shipment withoutId(String orderId, String deliveryAddress) {
        return new Shipment(null, orderId, deliveryAddress);
    }

    public static Shipment withId(ShipmentId id, String orderId, String deliveryAddress) {
        return new Shipment(id, orderId, deliveryAddress);
    }

    public Optional<ShipmentId> getId(){
        return Optional.ofNullable(id);
    }

    public void updateStatus(com.atn.digital.shipment.domain.models.ShipmentStatus newStatus, String reason) {
        if (isInvalidStatus(newStatus)) {
            return;
        }

        statusChangeHistory.add(new com.atn.digital.shipment.domain.models.ShipmentStatusChangeDetails(status, newStatus, Instant.now(), reason));
        status = newStatus;
    }

    private void init() {
        creationDate = Instant.now();
        status = ShipmentStatus.CREATED;
        statusChangeHistory.add(new ShipmentStatusChangeDetails(null, status, creationDate, "CREATION"));
    }

    public ShipmentStatusDetails getStatusDetails() {
        ShipmentStatusChangeDetails details =
            statusChangeHistory.stream().filter(s -> s.newStatus().equals(status)).toList().get(0);
        return new ShipmentStatusDetails(details.newStatus(), details.changeDate(), details.reason());
    }

    private boolean isInvalidStatus(ShipmentStatus newStatus) {
        return newStatus == null || newStatus == status || (status.getPriority() > newStatus.getPriority());
    }

    public static class ShipmentId extends SelfValidating<ShipmentId> {

        @NotNull
        @NotBlank
        @Getter
        private final String id;

        public ShipmentId(String id) {
            this.id = id;
            this.validateSelf();
        }
    }
}
