package com.atn.digital.user.domain.ports.in.queries;

import com.atn.digital.user.domain.models.Order;

import java.util.List;

public interface GetOrdersByUserIdQuery {
    List<Order> findOrdersByUserId(String userId);
}
