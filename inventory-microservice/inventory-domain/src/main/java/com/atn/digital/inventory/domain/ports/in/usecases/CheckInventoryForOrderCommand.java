package com.atn.digital.inventory.domain.ports.in.usecases;

import com.atn.digital.commons.validation.SelfValidating;
import com.atn.digital.inventory.domain.models.OrderItemData;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CheckInventoryForOrderCommand extends SelfValidating<CheckInventoryForOrderCommand> {

    @NotNull
    @NotBlank
    private final String orderId;

    @NotNull
    private final List<OrderItemData> items;

    public CheckInventoryForOrderCommand(String orderId, List<OrderItemData> items) {
        this.orderId = orderId;
        this.items = items;
        this.validateSelf();
    }
}
