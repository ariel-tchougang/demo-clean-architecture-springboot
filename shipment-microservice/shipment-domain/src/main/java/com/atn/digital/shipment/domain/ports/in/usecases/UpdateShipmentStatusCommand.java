package com.atn.digital.shipment.domain.ports.in.usecases;

import com.atn.digital.commons.validation.SelfValidating;
import com.atn.digital.shipment.domain.models.ShipmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class UpdateShipmentStatusCommand extends SelfValidating<UpdateShipmentStatusCommand> {

    @NotNull
    @NotBlank
    private final String shipmentId;

    @NotNull
    private final ShipmentStatus newStatus;

    @NotNull
    @NotBlank
    private final String reason;

    public UpdateShipmentStatusCommand(String shipmentId, ShipmentStatus newStatus, String reason) {
        this.shipmentId = shipmentId;
        this.newStatus = newStatus;
        this.reason = reason;
        this.validateSelf();
    }
}
