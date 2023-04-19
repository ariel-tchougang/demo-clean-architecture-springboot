package com.atn.digital.order.domain.ports.in.usecases;

import com.atn.digital.commons.validation.SelfValidating;
import com.atn.digital.order.domain.models.OrderStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusCommand extends SelfValidating<UpdateOrderStatusCommand> {

    @NotNull
    @NotBlank
    private final String orderId;

    @NotNull
    private final OrderStatus newStatus;

    @NotNull
    @NotBlank
    private final String reason;

    public UpdateOrderStatusCommand(String orderId, OrderStatus newStatus, String reason) {
        this.orderId = orderId;
        this.newStatus = newStatus;
        this.reason = reason;
        this.validateSelf();
    }
}
