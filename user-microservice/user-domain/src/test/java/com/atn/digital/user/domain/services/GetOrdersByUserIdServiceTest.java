package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.ports.in.queries.GetOrdersByUserIdQuery;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderCommand;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderUseCase;
import com.atn.digital.user.domain.ports.out.persistence.DummyOrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

class GetOrdersByUserIdServiceTest {

    private DummyOrderRepository repository = new DummyOrderRepository();
    private final AddNewOrderUseCase addNewOrder = new AddNewOrderService(repository);
    private GetOrdersByUserIdQuery service = new GetOrdersByUserIdService(repository);

    @Nested
    class WhenUserIdIsInvalidTest {
        @Test
        void shouldReturnEmptyListWhenUserIdIsNull() {
            List<Order> orders = service.findOrdersByUserId(null);
            Assertions.assertNotNull(orders);
            Assertions.assertTrue(orders.isEmpty());
        }

        @Test
        void shouldReturnEmptyListWhenUserIdIsBlank() {
            List<Order> orders = service.findOrdersByUserId("");
            Assertions.assertNotNull(orders);
            Assertions.assertTrue(orders.isEmpty());

            service.findOrdersByUserId(" ");
            Assertions.assertNotNull(orders);
            Assertions.assertTrue(orders.isEmpty());
        }
    }

    @Nested
    class WhenUserIdIsValidTest {
        @Test
        void shouldReturnEmptyListWhenNothingIsFound() {
            repository.clearOrders();
            List<Order> orders = service.findOrdersByUserId("userId");
            Assertions.assertNotNull(orders);
            Assertions.assertTrue(orders.isEmpty());
        }

        @Test
        void shouldReturnOrdersList() {
            repository.clearOrders();
            AddNewOrderCommand createCommand =
                    new AddNewOrderCommand("orderId", "userId", "address", new ArrayList<>(), BigDecimal.ZERO);
            addNewOrder.handle(createCommand);
            List<Order> orders = service.findOrdersByUserId("userId");
            Assertions.assertNotNull(orders);
            Assertions.assertEquals(1, orders.size());
            Order order = orders.get(0);
            Assertions.assertEquals(createCommand.getOrderId(), order.id());
            Assertions.assertEquals(createCommand.getUserId(), order.userId());
        }
    }

    @AfterEach
    void tearDown() {
        repository.clearOrders();
    }
}
