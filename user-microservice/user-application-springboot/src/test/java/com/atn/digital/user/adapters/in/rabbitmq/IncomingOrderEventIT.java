package com.atn.digital.user.adapters.in.rabbitmq;

import com.atn.digital.user.OutboundAdaptersExtension;
import com.atn.digital.user.adapters.config.rabbitmq.RabbitMQConfiguration;
import com.atn.digital.user.config.OrderDomainConfig;
import com.atn.digital.user.config.UserDomainConfig;
import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.ports.in.queries.GetOrdersByUserIdQuery;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserCommand;
import com.atn.digital.user.domain.ports.in.usecases.RegisterNewUserUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@Import({ UserDomainConfig.class, OrderDomainConfig.class, RabbitMQConfiguration.class })
@ExtendWith(OutboundAdaptersExtension.class)
class IncomingOrderEventIT {

    @Autowired
    private OrderEventRabbitMQSender sender;

    @Autowired
    private RegisterNewUserUseCase registerNewUser;

    @Autowired
    private GetOrdersByUserIdQuery query;

    @Test
    void shouldHandleIncomingOrderEvents() {

        RegisterNewUserCommand newUserCmd = new RegisterNewUserCommand(
                "Homer",
                "Simpson",
                "homer.simpson@unit.test"
        );

        var item = registerNewUser.handle(newUserCmd);
        Assertions.assertNotNull(item);
        Assertions.assertNotNull(item.getId());

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(
                "orderId", item.getId(), "deliveryAddress",
                Arrays.asList(
                        new OrderItemData("1", "item1", 5, BigDecimal.ONE),
                        new OrderItemData("2", "item2", 1, BigDecimal.TEN)),
                BigDecimal.valueOf(55L)
        );

        sender.publish(orderCreatedEvent);
        pause();

        List<Order> orders = query.findOrdersByUserId(item.getId());
        Assertions.assertNotNull(orders);
        Assertions.assertFalse(orders.isEmpty());
        Order order = orders.get(0);
        Assertions.assertNotNull(order);
        Assertions.assertEquals(item.getId(), order.userId());
        Assertions.assertEquals(orderCreatedEvent.orderId(), order.id());
        Assertions.assertEquals("CREATED", order.status());

        OrderStatusChangedEvent orderStatusChangedEvent = new OrderStatusChangedEvent(
                order.id(), item.getId(), order.deliveryAddress(),
                Arrays.asList(
                        new OrderItemData("1", "item1", 5, BigDecimal.ONE),
                        new OrderItemData("2", "item2", 1, BigDecimal.TEN)),
                "CREATED", "NEW_STATUS", "Test"
        );

        sender.publish(orderStatusChangedEvent);
        pause();

        order = query.findOrdersByUserId(item.getId()).get(0);
        Assertions.assertEquals(orderStatusChangedEvent.newStatus(), order.status());
        Assertions.assertEquals(orderStatusChangedEvent.reason(), order.message());
    }

    private static void pause() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //
        }
    }
}
