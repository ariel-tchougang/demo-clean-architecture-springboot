package com.atn.digital.shipment.adapters.out.persistence.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;

@DynamoDbBean
public class ShipmentEntity {

    private String id;
    private String orderId;
    private String deliveryAddress;
    private String status;
    private Long creationDate;
    private List<ShipmentItemEntity> items;
    private List<ShipmentStatusChangeDetailsEntity> statusChangeHistory;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Long creationDate) {
        this.creationDate = creationDate;
    }

    public List<ShipmentItemEntity> getItems() {
        return items;
    }

    public void setItems(List<ShipmentItemEntity> items) {
        this.items = items;
    }

    public List<ShipmentStatusChangeDetailsEntity> getStatusChangeHistory() {
        return statusChangeHistory;
    }

    public void setStatusChangeHistory(List<ShipmentStatusChangeDetailsEntity> statusChangeHistory) {
        this.statusChangeHistory = statusChangeHistory;
    }
}
