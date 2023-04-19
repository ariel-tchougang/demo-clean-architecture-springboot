package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.ports.in.queries.GetOrdersByUserIdQuery;
import com.atn.digital.user.domain.ports.out.persistence.GetOrdersByUserIdPort;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GetOrdersByUserIdService implements GetOrdersByUserIdQuery {

    private final GetOrdersByUserIdPort repository;

    @Override
    public List<Order> findOrdersByUserId(String userId) {

        if (userId == null || userId.isBlank()) {
            return new ArrayList<>();
        }

        List<Order> results = repository.findOrdersByUserId(userId);

        return results == null ? new ArrayList<>(): results;
    }
}
