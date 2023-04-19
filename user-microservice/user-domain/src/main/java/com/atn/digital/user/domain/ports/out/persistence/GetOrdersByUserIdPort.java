package com.atn.digital.user.domain.ports.out.persistence;

import com.atn.digital.user.domain.models.Order;

import java.util.List;

public interface GetOrdersByUserIdPort {
    List<Order> findOrdersByUserId(String userId);
}
