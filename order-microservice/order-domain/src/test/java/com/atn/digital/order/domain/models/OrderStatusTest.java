package com.atn.digital.order.domain.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderStatusTest {

    @Test
    void shouldReturnCreatedWhenOrderIsCreated() {
        Assertions.assertEquals(OrderStatus.CREATED, OrderStatus.getStatus("CREATED"));
    }

    @Test
    void shouldReturnInventoryCheckOkWhenInventoryCheckSuccessful() {
        Assertions.assertEquals(OrderStatus.INVENTORY_CHECK_OK, OrderStatus.getStatus("INVENTORY_CHECK_OK"));
    }

    @Test
    void shouldReturnPaymentCompletedWhenPaymentSuccessful() {
        Assertions.assertEquals(OrderStatus.PAYMENT_COMPLETED, OrderStatus.getStatus("PAYMENT_COMPLETED"));
    }

    @Test
    void shouldReturnProcessingInProgressWhenOrderReachesProcessingCenter() {
        Assertions.assertEquals(OrderStatus.PROCESSING_IN_PROGRESS, OrderStatus.getStatus("PROCESSING_IN_PROGRESS"));
    }

    @Test
    void shouldReturnPendingDeliveryWhenOrderIsShipped() {
        Assertions.assertEquals(OrderStatus.PENDING_DELIVERY, OrderStatus.getStatus("PENDING_DELIVERY"));
    }

    @Test
    void shouldReturnDeliveredWhenOrderIsDelivered() {
        Assertions.assertEquals(OrderStatus.DELIVERED, OrderStatus.getStatus("DELIVERED"));
    }

    @Test
    void shouldReturnCancelledWhenOrderIsCancelled() {
        Assertions.assertEquals(OrderStatus.CANCELLED, OrderStatus.getStatus("CANCELLED"));
    }

    @Test
    void shouldReturnUnknownWhenValueIsNotInEnum() {
        Assertions.assertEquals(OrderStatus.UNKNOWN, OrderStatus.getStatus("baDabouM"));
    }

    @Test
    void shouldBeNonCaseSensitive() {
        Assertions.assertEquals(OrderStatus.CANCELLED, OrderStatus.getStatus("cAnceLleD"));
        Assertions.assertEquals(OrderStatus.CREATED, OrderStatus.getStatus("CreATeD"));
    }
}
