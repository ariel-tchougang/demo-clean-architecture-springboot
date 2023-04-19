package com.atn.digital.order.domain.ports.in.usecases;

import com.atn.digital.order.domain.models.OrderStatusDetails;

public interface UpdateOrderStatusUseCase {
    OrderStatusDetails handle(UpdateOrderStatusCommand updateOrderStatusCommand);
}
