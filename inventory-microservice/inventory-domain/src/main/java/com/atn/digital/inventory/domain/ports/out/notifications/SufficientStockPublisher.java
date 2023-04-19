package com.atn.digital.inventory.domain.ports.out.notifications;

public interface SufficientStockPublisher {
    void publish(SufficientStockForOrderEvent event);
}
