package com.atn.digital.order.adapters.out.persistence.dynamodb;

import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.models.OrderStatusChangeDetails;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.util.ArrayList;
import java.util.List;

@DynamoDbBean
public class OrderEntity {

    private String id;
    private String customerId;
    private String deliveryAddress;
    private String status;
    private Long creationDate;
    private List<OrderItemEntity> items;
    private List<OrderStatusChangeDetailsEntity> statusChangeHistory;

    @DynamoDbPartitionKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public List<OrderItemEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderItemEntity> items) {
        this.items = items;
    }

    public List<OrderStatusChangeDetailsEntity> getStatusChangeHistory() {
        return statusChangeHistory;
    }

    public void setStatusChangeHistory(List<OrderStatusChangeDetailsEntity> statusChangeHistory) {
        this.statusChangeHistory = statusChangeHistory;
    }
}
