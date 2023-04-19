package com.atn.digital.shipment.application.adapters.in.web;

import com.atn.digital.shipment.domain.models.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateShipmentStatusWeb {
    private String shipmentId;
    private ShipmentStatus newStatus;
    private String reason;
}
