package com.atn.digital.order.domain.models;

import com.atn.digital.commons.validation.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem extends SelfValidating<OrderItem> {

    @NotNull
    @NotBlank
    private final String id;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    private final Integer quantity;

    @NotNull
    private final BigDecimal unitaryPrice;

    public OrderItem(String id, String name, Integer quantity, BigDecimal unitaryPrice) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unitaryPrice = unitaryPrice;
        this.validateSelf();
        requireStrictPositiveValue("quantity", quantity);
        requirePositiveValue("unitaryPrice", unitaryPrice);
    }
}
