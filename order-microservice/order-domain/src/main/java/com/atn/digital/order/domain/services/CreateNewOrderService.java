package com.atn.digital.order.domain.services;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderCommand;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderUseCase;
import com.atn.digital.order.domain.ports.out.notifications.OrderCreatedEvent;
import com.atn.digital.order.domain.ports.out.notifications.OrderCreatedEventPublisherPort;
import com.atn.digital.order.domain.ports.out.notifications.OrderItemData;
import com.atn.digital.order.domain.ports.out.persistence.CreateNewOrderPort;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreateNewOrderService implements CreateNewOrderUseCase {

    private final CreateNewOrderPort createNewOrderPort;
    private final OrderCreatedEventPublisherPort publishOrderCreatedPort;

    public OrderId handle(CreateNewOrderCommand newOrderCommand) {

        if (newOrderCommand == null) {
            throw new IllegalArgumentException("newOrderCommand must not be null!");
        }

        List<OrderItem> items = newOrderCommand.getItems().stream()
                .map(item -> new OrderItem(item.getId(), item.getName(), item.getQuantity(), item.getUnitaryPrice()))
                .toList();

        Order newOrder = Order.withoutId(newOrderCommand.getCustomerId(), newOrderCommand.getDeliveryAddress());
        newOrder.getItems().addAll(items);

        OrderId orderId = createNewOrderPort.createNewOrder(newOrder);

        publishOrderCreatedPort.publish(new OrderCreatedEvent(orderId.getId(),
                newOrder.getCustomerId(),
                newOrder.getDeliveryAddress(),
                items.stream().map(item -> new OrderItemData(
                        item.getId(),
                        item.getName(),
                        item.getQuantity(),
                        item.getUnitaryPrice())
                ).toList(),
                newOrder.getAmount())
        );

        return orderId;
    }
}
