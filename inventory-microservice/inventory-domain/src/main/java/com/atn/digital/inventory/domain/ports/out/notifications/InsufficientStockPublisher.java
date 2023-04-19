package com.atn.digital.inventory.domain.ports.out.notifications;

public interface InsufficientStockPublisher {
    void publish(InsufficientStockForOrderEvent event);
}
