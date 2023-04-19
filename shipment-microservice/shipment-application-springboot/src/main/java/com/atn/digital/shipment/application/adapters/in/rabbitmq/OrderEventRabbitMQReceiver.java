package com.atn.digital.shipment.application.adapters.in.rabbitmq;

import com.atn.digital.shipment.application.adapters.config.rabbitmq.OrderRabbitMQConfig;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentUseCase;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventRabbitMQReceiver implements DisposableBean {

    private final CreateNewShipmentUseCase useCase;
    private final SimpleMessageListenerContainer orderStatusChangedContainer;

    private final Gson gson = new Gson();

    @Autowired
    public OrderEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            OrderRabbitMQConfig config,
            AmqpAdmin amqpAdmin,
            CreateNewShipmentUseCase useCase) {

        this.useCase = useCase;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        Queue orderStatusChangedQueue = new Queue(config.orderStatusChangedQueueName(), false);
        Binding binding = BindingBuilder.bind(orderStatusChangedQueue)
                .to(exchange).with(config.orderStatusChangedRoutingKey());
        amqpAdmin.declareQueue(orderStatusChangedQueue);
        amqpAdmin.declareBinding(binding);

        orderStatusChangedContainer = new SimpleMessageListenerContainer();
        orderStatusChangedContainer.setConnectionFactory(connectionFactory);
        orderStatusChangedContainer.setQueueNames(orderStatusChangedQueue.getName());
        orderStatusChangedContainer.setMessageListener(message ->
                handleOrderStatusChangedMessage(new String(message.getBody())));
    }

    @PostConstruct
    private void initialize() {

        if (orderStatusChangedContainer != null) {
            orderStatusChangedContainer.start();
        }
    }

    public void destroy() throws Exception {

        if (orderStatusChangedContainer != null) {
            orderStatusChangedContainer.stop();
        }
    }

    private void handleOrderStatusChangedMessage(String message) {
        System.out.printf("Received message: %s%n", message);
        OrderStatusChangedEvent event = gson.fromJson(message, OrderStatusChangedEvent.class);

        if ("PAYMENT_COMPLETED".equals(event.newStatus())) {
            useCase.handle(
                    new CreateNewShipmentCommand(
                            event.orderId(),
                            event.deliveryAddress(),
                            event.items().stream()
                                    .map(item -> new ShipmentItem(item.id(), item.name(), item.quantity()))
                                    .toList()
                    ));
        }
    }
}
