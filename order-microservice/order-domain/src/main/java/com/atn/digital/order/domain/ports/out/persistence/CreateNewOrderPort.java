package com.atn.digital.order.domain.ports.out.persistence;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;

public interface CreateNewOrderPort {
    OrderId createNewOrder(Order newOrder);
}
