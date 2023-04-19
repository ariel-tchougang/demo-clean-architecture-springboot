package com.atn.digital.order.domain.services;

import com.atn.digital.order.domain.exceptions.OrderNotFoundException;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.models.OrderStatusDetails;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusCommand;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.atn.digital.order.domain.ports.out.notifications.InMemoryOrderEventPublisher;
import com.atn.digital.order.domain.ports.out.notifications.OrderItemData;
import com.atn.digital.order.domain.ports.out.notifications.OrderStatusChangedEvent;
import com.atn.digital.order.domain.ports.out.persistence.FailedLoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.FakeLoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.FakeUpdateOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.LoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.UpdateOrderPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateOrderStatusServiceTest {

    private final LoadOrderPort loadOrder = new FakeLoadOrderPort();
    private final UpdateOrderPort updateOrder = new FakeUpdateOrderPort();
    private final InMemoryOrderEventPublisher publisher = new InMemoryOrderEventPublisher();
    private final UpdateOrderStatusUseCase service = new UpdateOrderStatusService(loadOrder, updateOrder, publisher);
    private final LoadOrderPort failedLoader = new FailedLoadOrderPort();
    private final UpdateOrderStatusUseCase failedService = new UpdateOrderStatusService(failedLoader, updateOrder, publisher);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.handle(null));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenTargetOrderIsNotFound() {
        UpdateOrderStatusCommand command =
                new UpdateOrderStatusCommand("orderId", OrderStatus.PAYMENT_COMPLETED, "payment");
        Assertions.assertThrows(OrderNotFoundException.class, () -> failedService.handle(command));
    }

    @Test
    void shouldUpdateStatus() {
        UpdateOrderStatusCommand command =
                new UpdateOrderStatusCommand("orderId", OrderStatus.PAYMENT_COMPLETED, "payment");
        OrderStatusDetails details = service.handle(command);
        Assertions.assertEquals(OrderStatus.PAYMENT_COMPLETED, details.status());
    }

    @Test
    void shouldPublishOrderStatusChangedEvent() {
        UpdateOrderStatusCommand command =
                new UpdateOrderStatusCommand("orderId", OrderStatus.DELIVERED, "delivered");
        OrderStatusDetails details = service.handle(command);
        OrderStatusChangedEvent event = new OrderStatusChangedEvent(
                "orderId",
                "customerId",
    "deliveryAddress",
                Arrays.asList(new OrderItemData("1", "name", 1, BigDecimal.ONE)),
        "CREATED",
                command.getNewStatus().name(),
                command.getReason());
        assertThat(publisher.statusChangedOrderEvents()).containsExactly(event);
    }
}
