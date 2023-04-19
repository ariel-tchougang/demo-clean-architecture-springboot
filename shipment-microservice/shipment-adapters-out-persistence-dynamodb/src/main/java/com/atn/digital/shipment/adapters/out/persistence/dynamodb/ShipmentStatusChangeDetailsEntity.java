package com.atn.digital.shipment.adapters.out.persistence.dynamodb;

import com.atn.digital.shipment.domain.models.ShipmentStatus;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.time.Instant;

@DynamoDbBean
public class ShipmentStatusChangeDetailsEntity {

    private ShipmentStatus oldStatus;
    private ShipmentStatus newStatus;
    private Instant changeDate;
    private String reason;

    public static ShipmentStatusChangeDetailsEntity entity(
            ShipmentStatus oldStatus, ShipmentStatus newStatus, Instant changeDate, String reason) {
        ShipmentStatusChangeDetailsEntity entity = new ShipmentStatusChangeDetailsEntity();
        entity.oldStatus = oldStatus;
        entity.newStatus = newStatus;
        entity.changeDate = changeDate;
        entity.reason = reason;
        return entity;
    }

    public ShipmentStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(ShipmentStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public ShipmentStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(ShipmentStatus newStatus) {
        this.newStatus = newStatus;
    }

    public Instant getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Instant changeDate) {
        this.changeDate = changeDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
