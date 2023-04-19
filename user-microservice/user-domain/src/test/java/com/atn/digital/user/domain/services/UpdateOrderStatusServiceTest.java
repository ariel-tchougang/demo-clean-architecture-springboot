package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderCommand;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderUseCase;
import com.atn.digital.user.domain.ports.in.usecases.UpdateOrderStatusCommand;
import com.atn.digital.user.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.atn.digital.user.domain.ports.out.persistence.DummyOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

class UpdateOrderStatusServiceTest {

    private final DummyOrderRepository repository = new DummyOrderRepository();
    private final AddNewOrderUseCase addNewOrder = new AddNewOrderService(repository);
    private final UpdateOrderStatusUseCase service = new UpdateOrderStatusService(repository);

    @Nested
    class WhenUpdateOrderStatusCommandIsTest {
        @Test
        void shouldThrowIllegalArgumentException() {
            Assertions.assertThrows(IllegalArgumentException.class, () -> service.handle(null));
        }
    }

    @Nested
    class WhenEventIsOkTest {

        @Test
        void shouldUpdateStatus() {
            AddNewOrderCommand createCommand =
                    new AddNewOrderCommand("orderId", "userId", "address", new ArrayList<>(), BigDecimal.ZERO);
            addNewOrder.handle(createCommand);

            UpdateOrderStatusCommand command =
                    new UpdateOrderStatusCommand("orderId", "userId", "NEW_STATUS", null);
            service.handle(command);

            Order order = repository.findOrdersByUserId(command.getUserId()).stream()
                    .filter(o ->  command.getOrderId().equals(o.id()))
                    .findFirst().orElse(null);
            Assertions.assertNotNull(order);
            Assertions.assertEquals(command.getNewStatus(), order.status());
        }
    }

    @AfterEach
    void tearDown() {
        repository.clearOrders();
    }
}
