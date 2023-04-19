package com.atn.digital.order.application.adapters.out.notifications;

import com.atn.digital.order.domain.ports.out.notifications.OrderCreatedEventPublisherPort;
import com.atn.digital.order.domain.ports.out.notifications.OrderStatusChangedEventPublisherPort;

public abstract class OrderEventPublisherAdapter implements
        OrderCreatedEventPublisherPort, OrderStatusChangedEventPublisherPort { }
