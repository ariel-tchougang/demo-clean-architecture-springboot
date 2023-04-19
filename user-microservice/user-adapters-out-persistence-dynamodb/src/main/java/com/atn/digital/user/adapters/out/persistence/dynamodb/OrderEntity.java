package com.atn.digital.user.adapters.out.persistence.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.math.BigDecimal;
import java.util.List;

@DynamoDbBean
public class OrderEntity {

    private String id;

    private String userId;
    private String deliveryAddress;
    private List<OrderItemEntity> items;
    private BigDecimal amount;
    private String status;
    private String message;

    public static OrderEntity entity(String id, String userId, String deliveryAddress,
                                     List<OrderItemEntity> items, BigDecimal amount, String status, String message) {
        OrderEntity entity = new OrderEntity();
        entity.id = id;
        entity.userId = userId;
        entity.deliveryAddress = deliveryAddress;
        entity.items = items;
        entity.amount = amount;
        entity.status = status;
        entity.message = message;
        return entity;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getDeliveryAddress() { return deliveryAddress; }

    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public List<OrderItemEntity> getItems() { return items; }

    public void setItems(List<OrderItemEntity> items) { this.items = items; }

    public BigDecimal getAmount() { return amount; }

    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}
