package com.atn.digital.shipment.domain.models;

import com.atn.digital.commons.validation.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper=false)
public class ShipmentItem extends SelfValidating<ShipmentItem> {

    @NotNull
    @NotBlank
    private final String id;

    @NotNull
    @NotBlank
    private final String name;

    @NotNull
    private final Integer quantity;

    public ShipmentItem(String id, String name, Integer quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.validateSelf();
        requireStrictPositiveValue("quantity", quantity);
    }
}
