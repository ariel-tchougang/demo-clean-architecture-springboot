package com.atn.digital.order.domain.ports.in.usecases;

import com.atn.digital.commons.validation.SelfValidating;
import com.atn.digital.order.domain.models.OrderItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateNewOrderCommand extends SelfValidating<CreateNewOrderCommand> {

    @NotNull
    @NotBlank
    private final String customerId;

    @NotNull
    @NotBlank
    private final String deliveryAddress;

    @NotNull
    private final List<OrderItem> items;

    public CreateNewOrderCommand(String customerId, String deliveryAddress, List<OrderItem> items) {
        this.customerId = customerId;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
        this.validateSelf();
        requireStrictPositiveValue("items list size", items.size());
    }
}
