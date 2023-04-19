package com.atn.digital.inventory.application.adapters.in.rabbitmq;

import com.atn.digital.inventory.application.adapters.config.rabbitmq.OrderRabbitMQConfig;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderCommand;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderUseCase;
import com.google.gson.Gson;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Component
public class OrderCreatedEventRabbitMQReceiver implements DisposableBean {

    private final CheckInventoryForOrderUseCase useCase;
    private final SimpleMessageListenerContainer container;
    private final Gson gson = new Gson();

    @Autowired
    public OrderCreatedEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            OrderRabbitMQConfig config,
            AmqpAdmin amqpAdmin,
            CheckInventoryForOrderUseCase useCase) {

        this.useCase = useCase;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        Queue queue = new Queue(config.orderCreatedQueueName(), false);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(config.orderCreatedRoutingKey());
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);

        container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queue.getName());
        container.setMessageListener(message -> handleMessage(new String(message.getBody())));
    }

    @PostConstruct
    private void initialize() {
        if (container != null) {
            container.start();
        }
    }

    private void handleMessage(String message) {
        System.out.printf("Received message: %s%n", message);
        OrderCreatedEvent event = gson.fromJson(message, OrderCreatedEvent.class);
        CheckInventoryForOrderCommand command = new CheckInventoryForOrderCommand(
                event.orderId(),
                event.items());
        useCase.handle(command);
    }

    public void destroy() throws Exception {
        if (container != null) {
            container.stop();
        }
    }
}



