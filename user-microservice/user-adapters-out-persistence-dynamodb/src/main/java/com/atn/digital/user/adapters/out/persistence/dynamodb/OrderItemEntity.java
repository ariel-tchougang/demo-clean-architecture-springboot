package com.atn.digital.user.adapters.out.persistence.dynamodb;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class OrderItemEntity {

    private String id;
    private String name;
    private Integer quantity;

    public static OrderItemEntity entity(String id, String name, Integer quantity) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.id = id;
        entity.name = name;
        entity.quantity = quantity;
        return entity;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
