package com.atn.digital.order.domain.ports.out.persistence;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;

import java.util.Optional;

public class FailedLoadOrderPort implements LoadOrderPort {
    public Optional<Order> loadOrder(OrderId orderId) {
        return Optional.empty();
    }
}
