package com.atn.digital.order.domain.ports.out.persistence;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;

import java.util.Optional;

public interface LoadOrderPort {
    Optional<Order> loadOrder(OrderId orderId);
}
