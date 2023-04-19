package com.atn.digital.order.domain.services;

import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderCommand;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderUseCase;
import com.atn.digital.order.domain.ports.out.notifications.InMemoryOrderEventPublisher;
import com.atn.digital.order.domain.ports.out.notifications.OrderCreatedEvent;
import com.atn.digital.order.domain.ports.out.notifications.OrderItemData;
import com.atn.digital.order.domain.ports.out.persistence.CreateNewOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.DummyCreateNewOrderPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateNewOrderServiceTest {

    private final CreateNewOrderPort port = new DummyCreateNewOrderPort();
    private final InMemoryOrderEventPublisher publisher = new InMemoryOrderEventPublisher();

    private final CreateNewOrderUseCase service = new CreateNewOrderService(port, publisher);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.handle(null));
    }

    @Test
    void shouldReturnNewOrderId() {
        List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
        CreateNewOrderCommand command = new CreateNewOrderCommand("customerId", "address", items);
        OrderId bean = service.handle(command);
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(bean.getId());
    }

    @Test
    void shouldPublishOrderCreatedEvent() {
        List<OrderItem> items = Arrays.asList(new OrderItem("1", "name", 1, BigDecimal.ONE));
        CreateNewOrderCommand command = new CreateNewOrderCommand("customerId", "address", items);
        OrderId bean = service.handle(command);
        OrderCreatedEvent event = new OrderCreatedEvent(
                bean.getId(),
                "customerId",
                "address",
                Arrays.asList(new OrderItemData("1", "name", 1, BigDecimal.ONE)),
                BigDecimal.ONE
        );
        assertThat(publisher.createdOrderEvents()).containsExactly(event);
    }
}
