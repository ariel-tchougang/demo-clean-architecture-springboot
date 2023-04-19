package com.atn.digital.order.adapters.out.persistence.dynamodb;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.models.OrderStatusChangeDetails;

import java.time.Instant;

public class OrderMapper {

    public OrderEntity toOrderEntity(Order order) {

        if (order == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();

        if (order.getId().isPresent()) {
            entity.setId(order.getId().get().getId());
        }

        entity.setCustomerId(order.getCustomerId());
        entity.setDeliveryAddress(order.getDeliveryAddress());
        entity.setStatus(order.getStatus().name());
        entity.setCreationDate(order.getCreationDate().toEpochMilli());
        entity.setItems(order.getItems().stream()
                .map(item -> OrderItemEntity.entity(
                        item.getId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getUnitaryPrice()))
                .toList());
        entity.setStatusChangeHistory(order.getStatusChangeHistory().stream()
                .map(data -> OrderStatusChangeDetailsEntity.entity(
                        data.oldStatus(),
                        data.newStatus(),
                        data.changeDate(),
                        data.reason()))
                .toList());
        return entity;
    }

    public Order toOrder(OrderEntity entity) {

        if (entity == null) {
            return null;
        }

        Order order = Order.withId(
                new Order.OrderId(entity.getId()),
                entity.getCustomerId(),
                entity.getDeliveryAddress()
        );

        order.setCreationDate(Instant.ofEpochMilli(entity.getCreationDate()));
        order.setStatus(OrderStatus.getStatus(entity.getStatus()));
        order.getItems().clear();
        order.getItems().addAll(entity.getItems().stream()
                .map(item -> new OrderItem(
                        item.getId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getUnitaryPrice()))
                .toList());
        order.getStatusChangeHistory().clear();
        order.getStatusChangeHistory().addAll(entity.getStatusChangeHistory().stream()
                .map(data -> new OrderStatusChangeDetails(
                        data.getOldStatus(),
                        data.getNewStatus(),
                        data.getChangeDate(),
                        data.getReason()))
                .toList());

        return order;
    }
}
