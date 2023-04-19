package com.atn.digital.order.domain.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class OrderTest {

    @Nested
    class OnNewOrderInstantiation {
        @Test
        void statusIsCreated() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            Assertions.assertEquals(OrderStatus.CREATED, order.getStatus());
        }

        @Test
        void statusChangeHistoryIsInitializedWithStatusCreated() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            Assertions.assertEquals(1, order.getStatusChangeHistory().size());
            Assertions.assertEquals(OrderStatus.CREATED, order.getStatusChangeHistory().get(0).newStatus());
            Assertions.assertNull(order.getStatusChangeHistory().get(0).oldStatus());
        }

        @Test
        void amountIsZero() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            Assertions.assertEquals(BigDecimal.ZERO, order.getAmount());
        }

        @Test
        void statusDetailsReasonIsCreation() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            Assertions.assertEquals("CREATION", order.getStatusDetails().reason());
        }
    }

    @Nested
    class OnOrderItemAdded {
        @Test
        void amountIsSumOfItemsAmounts() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            OrderItem items1 = new OrderItem("1", "item1", 1, BigDecimal.TEN);
            OrderItem items2 = new OrderItem("2", "item2", 2, BigDecimal.ONE);
            order.getItems().add(items1);
            order.getItems().add(items2);
            Assertions.assertEquals(BigDecimal.valueOf(12L), order.getAmount());
        }
    }

    @Nested
    class OnUpdateStatus {
        @Test
        void statusUnchangedWhenNewValueIsNull() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            order.updateStatus(null, "reason");
            Assertions.assertEquals(OrderStatus.CREATED, order.getStatus());
        }

        @Test
        void statusUnchangedWhenNewValueIsIdenticalToOld() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            order.updateStatus(OrderStatus.CREATED, "reason");
            Assertions.assertEquals(OrderStatus.CREATED, order.getStatus());
            Assertions.assertEquals(1, order.getStatusChangeHistory().size());
        }

        @Test
        void statusUnchangedWhenNewValueHasInferiorPriority() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            order.updateStatus(OrderStatus.UNKNOWN, "reason");
            Assertions.assertEquals(OrderStatus.CREATED, order.getStatus());
            Assertions.assertEquals(1, order.getStatusChangeHistory().size());
        }

        @Test
        void statusIsEqualsToNewValue() {
            Order order = Order.withoutId("customerId", "deliveryAddress");
            order.updateStatus(OrderStatus.PAYMENT_COMPLETED, "reason");
            Assertions.assertEquals(OrderStatus.PAYMENT_COMPLETED, order.getStatus());
            Assertions.assertEquals(2, order.getStatusChangeHistory().size());
            Assertions.assertEquals(OrderStatus.PAYMENT_COMPLETED,
                    order.getStatusChangeHistory().get(1).newStatus());
        }
    }
}
