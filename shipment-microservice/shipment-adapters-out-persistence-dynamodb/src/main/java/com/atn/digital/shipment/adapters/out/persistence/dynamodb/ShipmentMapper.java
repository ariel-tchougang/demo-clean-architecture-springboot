package com.atn.digital.shipment.adapters.out.persistence.dynamodb;

import com.atn.digital.shipment.domain.models.Shipment;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import com.atn.digital.shipment.domain.models.ShipmentStatus;
import com.atn.digital.shipment.domain.models.ShipmentStatusChangeDetails;

import java.time.Instant;

public class ShipmentMapper {

    public ShipmentEntity toShipmentEntity(Shipment shipment) {

        if (shipment == null) {
            return null;
        }

        ShipmentEntity entity = new ShipmentEntity();

        if (shipment.getId().isPresent()) {
            entity.setId(shipment.getId().get().getId());
        }

        entity.setOrderId(shipment.getOrderId());
        entity.setDeliveryAddress(shipment.getDeliveryAddress());
        entity.setStatus(shipment.getStatus().name());
        entity.setCreationDate(shipment.getCreationDate().toEpochMilli());
        entity.setItems(shipment.getItems().stream()
                .map(item -> ShipmentItemEntity.entity(
                        item.getId(),
                        item.getName(),
                        item.getQuantity()))
                .toList());
        entity.setStatusChangeHistory(shipment.getStatusChangeHistory().stream()
                .map(data -> ShipmentStatusChangeDetailsEntity.entity(
                        data.oldStatus(),
                        data.newStatus(),
                        data.changeDate(),
                        data.reason()))
                .toList());
        return entity;
    }

    public Shipment toShipment(ShipmentEntity entity) {

        if (entity == null) {
            return null;
        }

        Shipment order = Shipment.withId(
                new ShipmentId(entity.getId()),
                entity.getOrderId(),
                entity.getDeliveryAddress()
        );

        order.setCreationDate(Instant.ofEpochMilli(entity.getCreationDate()));
        order.setStatus(ShipmentStatus.getStatus(entity.getStatus()));
        order.getItems().clear();
        order.getItems().addAll(entity.getItems().stream()
                .map(item -> new ShipmentItem(
                        item.getId(),
                        item.getName(),
                        item.getQuantity()))
                .toList());
        order.getStatusChangeHistory().clear();
        order.getStatusChangeHistory().addAll(entity.getStatusChangeHistory().stream()
                .map(data -> new ShipmentStatusChangeDetails(
                        data.getOldStatus(),
                        data.getNewStatus(),
                        data.getChangeDate(),
                        data.getReason()))
                .toList());
        return order;
    }
}
