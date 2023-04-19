package com.atn.digital.order.domain.services;

import com.atn.digital.order.domain.exceptions.OrderNotFoundException;
import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatusChangeDetails;
import com.atn.digital.order.domain.ports.in.queries.GetOrderStatusChangeDetailsHistoryQuery;
import com.atn.digital.order.domain.ports.out.persistence.LoadOrderPort;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class GetOrderStatusChangeDetailsHistoryService implements GetOrderStatusChangeDetailsHistoryQuery {

    private final LoadOrderPort port;

    public List<OrderStatusChangeDetails> getStatusChangeDetailsHistory(OrderId orderId) {

        if (orderId == null) {
            throw new IllegalArgumentException("orderId must not be null!");
        }

        Optional<Order> optional = port.loadOrder(orderId);

        if (optional.isEmpty()) {
            throw new OrderNotFoundException("orderId not found: " + orderId);
        }

        return optional.isEmpty() ? new ArrayList<>() : optional.get().getStatusChangeHistory();
    }
}
