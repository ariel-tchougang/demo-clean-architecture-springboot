package com.atn.digital.order.domain.ports.in.queries;

import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatusChangeDetails;

import java.util.List;

public interface GetOrderStatusChangeDetailsHistoryQuery {
    List<OrderStatusChangeDetails> getStatusChangeDetailsHistory(OrderId orderId);
}
