package com.atn.digital.inventory.domain.ports.out.notifications;

import java.util.ArrayList;
import java.util.List;

public class DummyStockEventPublisher implements SufficientStockPublisher, InsufficientStockPublisher {

    private final List<SufficientStockForOrderEvent> sufficientStockEvents = new ArrayList<>();
    private final List<InsufficientStockForOrderEvent> insufficientStockEvents = new ArrayList<>();

    public void publish(SufficientStockForOrderEvent event) {
        sufficientStockEvents.add(event);
        System.out.println("EnoughStockForOrderEvent: " + event.orderId());
    }

    public void publish(InsufficientStockForOrderEvent event) {
        insufficientStockEvents.add(event);
        System.err.println("InsufficientStockForOrderEvent: " + event.orderId());
    }

    public List<SufficientStockForOrderEvent> sufficientStockEvents() {
        return sufficientStockEvents;
    }

    public List<InsufficientStockForOrderEvent> insufficientStockEvents() {
        return insufficientStockEvents;
    }
}
