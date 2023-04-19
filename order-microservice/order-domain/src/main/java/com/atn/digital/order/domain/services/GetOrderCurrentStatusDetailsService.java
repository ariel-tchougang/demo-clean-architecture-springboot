package com.atn.digital.order.domain.services;

import com.atn.digital.order.domain.exceptions.OrderNotFoundException;
import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatusDetails;
import com.atn.digital.order.domain.ports.in.queries.GetOrderCurrentStatusDetailsQuery;
import com.atn.digital.order.domain.ports.out.persistence.LoadOrderPort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class GetOrderCurrentStatusDetailsService implements GetOrderCurrentStatusDetailsQuery {

    private final LoadOrderPort port;

    public Optional<OrderStatusDetails> getStatusDetails(OrderId orderId) {

        if (orderId == null) {
            throw new IllegalArgumentException("orderId must not be null!");
        }

        Optional<Order> optional = port.loadOrder(orderId);

        if (optional.isEmpty()) {
            throw new OrderNotFoundException("orderId not found: " + orderId);
        }

        return optional.isEmpty() ? Optional.empty() : Optional.of(optional.get().getStatusDetails());
    }
}
