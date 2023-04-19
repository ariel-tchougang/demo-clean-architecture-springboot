package com.atn.digital.user.adapters.in.rabbitmq;

import com.atn.digital.user.adapters.config.rabbitmq.OrderRabbitMQConfig;
import com.atn.digital.user.domain.models.OrderItem;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderCommand;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderUseCase;
import com.atn.digital.user.domain.ports.in.usecases.UpdateOrderStatusCommand;
import com.atn.digital.user.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventRabbitMQReceiver implements DisposableBean {

    private final AddNewOrderUseCase addNewOrder;
    private final UpdateOrderStatusUseCase updateOrder;
    private final SimpleMessageListenerContainer orderCreatedContainer;
    private final SimpleMessageListenerContainer orderStatusChangedContainer;
    private final Gson gson = new Gson();

    @Autowired
    public OrderEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            OrderRabbitMQConfig config,
            AmqpAdmin amqpAdmin,
            AddNewOrderUseCase addNewOrder,
            UpdateOrderStatusUseCase updateOrder) {

        this.addNewOrder = addNewOrder;
        this.updateOrder = updateOrder;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        Queue orderCreatedQueue = new Queue(config.orderCreatedQueueName(), false);
        Binding binding = BindingBuilder.bind(orderCreatedQueue)
                .to(exchange).with(config.orderCreatedRoutingKey());
        amqpAdmin.declareQueue(orderCreatedQueue);
        amqpAdmin.declareBinding(binding);

        Queue orderStatusChangedQueue = new Queue(config.orderStatusChangedQueueName(), false);
        Binding binding2 = BindingBuilder.bind(orderStatusChangedQueue)
                .to(exchange).with(config.orderStatusChangedRoutingKey());
        amqpAdmin.declareQueue(orderStatusChangedQueue);
        amqpAdmin.declareBinding(binding2);

        orderCreatedContainer = new SimpleMessageListenerContainer();
        orderCreatedContainer.setConnectionFactory(connectionFactory);
        orderCreatedContainer.setQueueNames(orderCreatedQueue.getName());
        orderCreatedContainer.setMessageListener(message ->
                handleOrderCreatedMessage(new String(message.getBody())));

        orderStatusChangedContainer = new SimpleMessageListenerContainer();
        orderStatusChangedContainer.setConnectionFactory(connectionFactory);
        orderStatusChangedContainer.setQueueNames(orderStatusChangedQueue.getName());
        orderStatusChangedContainer.setMessageListener(message ->
                handleOrderStatusChangedMessage(new String(message.getBody())));
    }

    @PostConstruct
    private void initialize() {
        if (orderCreatedContainer != null) {
            orderCreatedContainer.start();
        }

        if (orderStatusChangedContainer != null) {
            orderStatusChangedContainer.start();
        }
    }

    public void destroy() throws Exception {
        if (orderCreatedContainer != null) {
            orderCreatedContainer.stop();
        }

        if (orderStatusChangedContainer != null) {
            orderStatusChangedContainer.stop();
        }
    }

    private void handleOrderCreatedMessage(String message) {
        try {
            System.out.printf("Received message: %s%n", message);
            OrderCreatedEvent event = gson.fromJson(message, OrderCreatedEvent.class);
            AddNewOrderCommand command = new AddNewOrderCommand(
                    event.orderId(),
                    event.userId(),
                    event.deliveryAddress(),
                    event.items().stream()
                            .map(item -> new OrderItem(item.id(), item.name(), item.quantity()))
                            .toList(),
                    event.amount());
            addNewOrder.handle(command);
        } catch (Exception e) {
            System.err.printf("An error occurred when processing incoming message: %s%n", message);
            System.err.println(e.getMessage());
        }
    }

    private void handleOrderStatusChangedMessage(String message) {
        try {
            System.out.printf("Received message: %s%n", message);
            OrderStatusChangedEvent event = gson.fromJson(message, OrderStatusChangedEvent.class);
            UpdateOrderStatusCommand command = new UpdateOrderStatusCommand(
                    event.orderId(),
                    event.userId(),
                    event.newStatus(),
                    event.reason()
            );
            updateOrder.handle(command);
        } catch (Exception e) {
            System.err.printf("An error occurred when processing incoming message: %s%n", message);
            System.err.println(e.getMessage());
        }
    }
}
