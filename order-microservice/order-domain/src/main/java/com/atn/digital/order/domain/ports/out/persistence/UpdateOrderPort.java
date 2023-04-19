package com.atn.digital.order.domain.ports.out.persistence;

import com.atn.digital.order.domain.models.Order;

public interface UpdateOrderPort {
    Order update(Order order);
}
