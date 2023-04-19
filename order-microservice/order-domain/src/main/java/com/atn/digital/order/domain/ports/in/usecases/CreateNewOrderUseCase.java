package com.atn.digital.order.domain.ports.in.usecases;

import com.atn.digital.order.domain.models.Order.OrderId;

public interface CreateNewOrderUseCase {
    OrderId handle(CreateNewOrderCommand newOrderCommand);
}
