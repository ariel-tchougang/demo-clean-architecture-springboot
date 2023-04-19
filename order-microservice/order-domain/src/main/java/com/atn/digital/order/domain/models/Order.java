package com.atn.digital.order.domain.models;

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
public class Order {

    private final OrderId id;
    private final String customerId;
    private final String deliveryAddress;

    private OrderStatus status;
    private Instant creationDate;

    private final List<OrderItem> items = new ArrayList<>();
    private final List<OrderStatusChangeDetails> statusChangeHistory = new ArrayList<>();

    private Order(OrderId id, String customerId, String deliveryAddress) {
        this.id = id;
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
        init();
    }

    public static Order withoutId(String customerId, String deliveryAddress) {
        return new Order(null, customerId, deliveryAddress);
    }

    public static Order withId(OrderId id, String customerId, String deliveryAddress) {
        return new Order(id, customerId, deliveryAddress);
    }

    public Optional<OrderId> getId(){
        return Optional.ofNullable(id);
    }

    public BigDecimal getAmount() {
        return items.isEmpty() ? BigDecimal.ZERO :
                items.stream().map(item -> item.getUnitaryPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateStatus(OrderStatus newStatus, String reason) {
        if (isInvalidStatus(newStatus)) {
            return;
        }

        statusChangeHistory.add(new OrderStatusChangeDetails(status, newStatus, Instant.now(), reason));
        status = newStatus;
    }

    private void init() {
        creationDate = Instant.now();
        status = OrderStatus.CREATED;
        statusChangeHistory.add(new OrderStatusChangeDetails(null, status, creationDate, "CREATION"));
    }

    public OrderStatusDetails getStatusDetails() {
        OrderStatusChangeDetails details =
            statusChangeHistory.stream().filter(s -> s.newStatus().equals(status)).toList().get(0);
        return new OrderStatusDetails(details.newStatus(), details.changeDate(), details.reason());
    }

    private boolean isInvalidStatus(OrderStatus newStatus) {
        return newStatus == null || newStatus == status || (status.getPriority() > newStatus.getPriority());
    }

    public static class OrderId extends SelfValidating<OrderId> {

        @NotNull
        @NotBlank
        @Getter
        private final String id;

        public OrderId(String id) {
            this.id = id;
            this.validateSelf();
        }
    }
}
