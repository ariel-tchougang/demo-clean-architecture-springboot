package com.atn.digital.order.domain.services;

import com.atn.digital.order.domain.exceptions.OrderNotFoundException;
import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.models.OrderStatusDetails;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusCommand;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.atn.digital.order.domain.ports.out.notifications.OrderItemData;
import com.atn.digital.order.domain.ports.out.notifications.OrderStatusChangedEvent;
import com.atn.digital.order.domain.ports.out.notifications.OrderStatusChangedEventPublisherPort;
import com.atn.digital.order.domain.ports.out.persistence.LoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.UpdateOrderPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class UpdateOrderStatusService implements UpdateOrderStatusUseCase {

    private final LoadOrderPort loadOrderPort;
    private final UpdateOrderPort updateOrderPort;
    private final OrderStatusChangedEventPublisherPort publisherPort;

    public OrderStatusDetails handle(UpdateOrderStatusCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("command must not be null!");
        }

        Optional<Order> optional = loadOrderPort.loadOrder(new OrderId(command.getOrderId()));

        if (optional.isEmpty()) {
            throw new OrderNotFoundException("orderId not found: " + command.getOrderId());
        }

        Order order = optional.get();
        OrderStatus oldStatus = order.getStatus();

        order.updateStatus(command.getNewStatus(), command.getReason());

        OrderStatusDetails newStatusDetails = updateOrderPort.update(order).getStatusDetails();

        if (newStatusDetails.status() != oldStatus) {
            publisherPort.publish(
                new OrderStatusChangedEvent(
                    command.getOrderId(),
                    order.getCustomerId(),
                    order.getDeliveryAddress(),
                    order.getItems().stream().map(item -> new OrderItemData(
                            item.getId(),
                            item.getName(),
                            item.getQuantity(),
                            item.getUnitaryPrice())
                    ).toList(),
                    oldStatus.name(),
                    newStatusDetails.status().name(),
                    command.getReason()
                )
            );
        }

        return newStatusDetails;
    }
}
