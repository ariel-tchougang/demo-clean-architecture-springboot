package com.atn.digital.user.domain.ports.out.persistence;

import com.atn.digital.user.domain.models.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyOrderRepository implements AddNewOrderPort, UpdateOrderStatusPort, GetOrdersByUserIdPort{

    private final Map<String, Order> orders = new HashMap<>();

    public void addNewOrder(Order order) {
        orders.put(order.id(), order);
    }

    public List<Order> findOrdersByUserId(String userId) {
        return orders.values().stream().filter(order -> order.userId().equals(userId)).toList();
    }

    public void updateStatus(String userId, String orderId, String status, String reason) {
        Order dbOrder = orders.get(orderId);

        if (dbOrder != null) {
            Order order = new Order(
                    orderId,
                    userId,
                    dbOrder.deliveryAddress(),
                    dbOrder.items(),
                    dbOrder.amount(),
                    status,
                    reason
            );

            orders.put(order.id(), order);
        }
    }

    public void clearOrders() {
        orders.clear();
    }
}
