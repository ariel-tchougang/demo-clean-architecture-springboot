package com.atn.digital.order.domain.ports.out.notifications;

import java.util.ArrayList;
import java.util.List;

public class InMemoryOrderEventPublisher
        implements OrderCreatedEventPublisherPort, OrderStatusChangedEventPublisherPort {

    private final List<OrderCreatedEvent> createdOrderEvents = new ArrayList<>();
    private final List<OrderStatusChangedEvent> statusChangedOrderEvents = new ArrayList<>();

    public void publish(OrderCreatedEvent event) {
        createdOrderEvents.add(event);
        System.out.printf("OrderCreatedEvent [id=%s]%n", event.orderId());
    }

    public void publish(OrderStatusChangedEvent event) {
        statusChangedOrderEvents.add(event);
        System.out.printf("OrderStatusChangedEvent [id=%s, oldStatus=%s, newStatus=%s]%n",
                event.orderId(),
                event.oldStatus(),
                event.newStatus());
    }

    public List<OrderCreatedEvent> createdOrderEvents() {
        return createdOrderEvents;
    }

    public List<OrderStatusChangedEvent> statusChangedOrderEvents() {
        return statusChangedOrderEvents;
    }
}
