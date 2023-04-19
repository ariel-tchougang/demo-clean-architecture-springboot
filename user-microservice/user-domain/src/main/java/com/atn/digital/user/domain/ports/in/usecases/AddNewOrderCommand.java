package com.atn.digital.user.domain.ports.in.usecases;

import com.atn.digital.commons.validation.SelfValidating;
import com.atn.digital.user.domain.models.OrderItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class AddNewOrderCommand extends SelfValidating<AddNewOrderCommand> {

    @NotNull
    @NotBlank
    private final String orderId;

    @NotNull
    @NotBlank
    private final String userId;

    @NotNull
    @NotBlank
    private final String deliveryAddress;

    @NotNull
    private final List<OrderItem> items;

    @NotNull
    private final BigDecimal amount;

    public AddNewOrderCommand(String orderId, String userId, String deliveryAddress, List<OrderItem> items, BigDecimal amount) {
        this.orderId = orderId;
        this.userId = userId;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
        this.amount = amount;
        this.validateSelf();
    }
}
