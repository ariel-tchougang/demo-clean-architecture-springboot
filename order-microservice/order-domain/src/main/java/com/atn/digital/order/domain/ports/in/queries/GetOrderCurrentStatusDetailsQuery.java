package com.atn.digital.order.domain.ports.in.queries;

import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatusDetails;

import java.util.Optional;

public interface GetOrderCurrentStatusDetailsQuery {
    Optional<OrderStatusDetails> getStatusDetails(OrderId orderId);
}
