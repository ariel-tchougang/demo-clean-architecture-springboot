package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderCommand;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderUseCase;
import com.atn.digital.user.domain.ports.out.persistence.DummyOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class AddNewOrderServiceTest {

    private final DummyOrderRepository repository = new DummyOrderRepository();
    private final AddNewOrderUseCase service = new AddNewOrderService(repository);

    @Nested
    class WhenAddNewOrderCommandIsNullTest {
        @Test
        void shouldThrowIllegalArgumentException() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> service.handle(null));
        }
    }

    @Nested
    class WhenAddNewOrderCommandIsOkTest {
        @Test
        void shouldAddNewOrder() {
            AddNewOrderCommand command =
                    new AddNewOrderCommand("orderId", "userId", "address", new ArrayList<>(), BigDecimal.ZERO);
            service.handle(command);
            List<Order> orders = repository.findOrdersByUserId(command.getUserId());
            Assertions.assertNotNull(orders);
            Assertions.assertFalse(orders.isEmpty());
            Optional<Order> optional = orders.stream()
                    .filter(order ->  command.getOrderId().equals(order.id()))
                    .findFirst();
            Assertions.assertTrue(optional.isPresent());
        }

        @Test
        void newOrderStatusShouldBeCreated() {
            AddNewOrderCommand command =
                    new AddNewOrderCommand("orderId", "userId", "address", new ArrayList<>(), BigDecimal.ZERO);
            service.handle(command);
            Order order = repository.findOrdersByUserId(command.getUserId()).stream()
                    .filter(o ->  command.getOrderId().equals(o.id()))
                    .findFirst().orElse(null);
            Assertions.assertNotNull(order);
            Assertions.assertEquals("CREATED", order.status());
        }
    }
}
