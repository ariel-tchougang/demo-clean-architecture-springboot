package com.atn.digital.order.domain.ports.out.persistence;

import com.atn.digital.order.domain.models.Order;

public class FakeUpdateOrderPort implements UpdateOrderPort {
    public Order update(Order order) {
        return order;
    }
}
