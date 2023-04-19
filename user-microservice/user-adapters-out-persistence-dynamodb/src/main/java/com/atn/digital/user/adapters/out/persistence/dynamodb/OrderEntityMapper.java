package com.atn.digital.user.adapters.out.persistence.dynamodb;

import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.models.OrderItem;

public class OrderEntityMapper {

    public Order toOrder(OrderEntity entity) {
        return (entity == null) ? null : new Order(
                entity.getId(),
                entity.getUserId(),
                entity.getDeliveryAddress(),
                entity.getItems().stream()
                        .map(item -> new OrderItem(item.getId(), item.getName(), item.getQuantity()))
                        .toList(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getMessage()
        );
    }

    public OrderEntity toOrderEntity(Order order) {
        return (order == null) ? null : OrderEntity.entity(
                order.id(),
                order.userId(),
                order.deliveryAddress(),
                order.items().stream()
                        .map(item -> OrderItemEntity.entity(item.id(), item.name(), item.quantity()))
                        .toList(),
                order.amount(),
                order.status(),
                order.message()
        );
    }
}
