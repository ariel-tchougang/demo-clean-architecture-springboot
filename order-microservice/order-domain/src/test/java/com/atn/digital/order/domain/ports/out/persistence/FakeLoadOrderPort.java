package com.atn.digital.order.domain.ports.out.persistence;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderItem;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class FakeLoadOrderPort implements LoadOrderPort {
    public Optional<Order> loadOrder(OrderId orderId) {
        List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
        Order order = Order.withId(orderId, "customerId", "deliveryAddress");
        order.getItems().addAll(items);
        return Optional.of(order);
    }
}
