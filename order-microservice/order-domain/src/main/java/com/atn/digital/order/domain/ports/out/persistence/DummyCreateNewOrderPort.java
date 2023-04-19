package com.atn.digital.order.domain.ports.out.persistence;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;

import java.util.UUID;

public class DummyCreateNewOrderPort implements CreateNewOrderPort {
    public OrderId createNewOrder(Order newOrder) {
        Order dbOrder = Order.withId(
                new OrderId(UUID.randomUUID().toString()),
                newOrder.getCustomerId(),
                newOrder.getDeliveryAddress()
        );
        dbOrder.getItems().addAll(newOrder.getItems());
        return dbOrder.getId().get();
    }
}
