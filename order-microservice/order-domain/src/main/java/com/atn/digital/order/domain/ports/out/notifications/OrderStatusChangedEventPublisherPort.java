package com.atn.digital.order.domain.ports.out.notifications;

public interface OrderStatusChangedEventPublisherPort {
    void publish(OrderStatusChangedEvent event);
}
