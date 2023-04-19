package com.atn.digital.inventory.domain.services;

import com.atn.digital.inventory.domain.models.OrderItemData;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderCommand;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderUseCase;
import com.atn.digital.inventory.domain.ports.out.notifications.DummyStockEventPublisher;
import com.atn.digital.inventory.domain.ports.out.notifications.InsufficientStockForOrderEvent;
import com.atn.digital.inventory.domain.ports.out.notifications.SufficientStockForOrderEvent;
import com.atn.digital.inventory.domain.ports.out.persistence.DummyLoadStockItemQuantityPort;
import com.atn.digital.inventory.domain.ports.out.persistence.LoadStockItemQuantityPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CheckInventoryForOrderServiceTest {

    private final LoadStockItemQuantityPort port = new DummyLoadStockItemQuantityPort();
    private final DummyStockEventPublisher publisher = new DummyStockEventPublisher();
    private CheckInventoryForOrderUseCase service = new CheckInventoryForOrderService(port, publisher, publisher);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.handle(null));
    }

    @Test
    void shouldReturnFalseWhenInsufficientStock() {
        CheckInventoryForOrderCommand command = new CheckInventoryForOrderCommand(
                "orderId",
                Arrays.asList(new OrderItemData("orderId", "name", 10, BigDecimal.ONE))
        );

        Assertions.assertFalse(service.handle(command));
    }

    @Test
    void shouldReturnTrueWhenSufficientStock() {
        CheckInventoryForOrderCommand command = new CheckInventoryForOrderCommand(
                "orderId",
                Arrays.asList(new OrderItemData("orderId", "name", 2, BigDecimal.ONE))
        );

        Assertions.assertTrue(service.handle(command));
    }

    @Test
    void shouldPublishSufficientStockForOrderEventWhenSufficientStock() {
        List<OrderItemData> items = Arrays.asList(new OrderItemData("orderId", "name", 2, BigDecimal.ONE));
        CheckInventoryForOrderCommand command = new CheckInventoryForOrderCommand("orderId", items);
        Assertions.assertTrue(service.handle(command));
        SufficientStockForOrderEvent event = new SufficientStockForOrderEvent("orderId");
        assertThat(publisher.sufficientStockEvents()).containsExactly(event);
    }

    @Test
    void shouldPublishInsufficientStockForOrderEventWhenInsufficientStock() {
        List<OrderItemData> items = Arrays.asList(new OrderItemData("orderId", "name", 10, BigDecimal.ONE));
        CheckInventoryForOrderCommand command = new CheckInventoryForOrderCommand("orderId", items);
        Assertions.assertFalse(service.handle(command));
        InsufficientStockForOrderEvent event = new InsufficientStockForOrderEvent("orderId", items);
        assertThat(publisher.insufficientStockEvents()).containsExactly(event);
    }

}
