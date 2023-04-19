package com.atn.digital.order.adapters.out.persistence.dynamodb;

import com.atn.digital.order.domain.models.OrderStatus;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.math.BigDecimal;
import java.time.Instant;

@DynamoDbBean
public class OrderStatusChangeDetailsEntity  {

    private OrderStatus oldStatus;
    private OrderStatus newStatus;
    private Instant changeDate;
    private String reason;

    public static OrderStatusChangeDetailsEntity entity(
            OrderStatus oldStatus, OrderStatus newStatus, Instant changeDate, String reason) {
        OrderStatusChangeDetailsEntity entity = new OrderStatusChangeDetailsEntity();
        entity.oldStatus = oldStatus;
        entity.newStatus = newStatus;
        entity.changeDate = changeDate;
        entity.reason = reason;
        return entity;
    }

    public OrderStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(OrderStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderStatus newStatus) {
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
