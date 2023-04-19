package com.atn.digital.shipment.domain.ports.in.usecases;

import com.atn.digital.commons.validation.SelfValidating;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CreateNewShipmentCommand extends SelfValidating<CreateNewShipmentCommand> {

    @NotNull
    @NotBlank
    private final String orderId;

    @NotNull
    @NotBlank
    private final String deliveryAddress;

    @NotNull
    private final List<ShipmentItem> items;

    public CreateNewShipmentCommand(String orderId, String deliveryAddress, List<ShipmentItem> items) {
        this.orderId = orderId;
        this.deliveryAddress = deliveryAddress;
        this.items = items;
        this.validateSelf();
        requireStrictPositiveValue("items list size", items.size());
    }
}
