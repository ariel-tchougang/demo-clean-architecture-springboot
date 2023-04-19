package com.atn.digital.order.domain.ports.out.notifications;

public interface OrderCreatedEventPublisherPort {
    void publish(OrderCreatedEvent event);
}
