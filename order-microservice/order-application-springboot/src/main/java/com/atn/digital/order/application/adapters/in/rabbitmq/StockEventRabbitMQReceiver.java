package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.adapters.config.rabbitmq.InventoryRabbitMQConfig;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusCommand;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusUseCase;
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
public class StockEventRabbitMQReceiver implements DisposableBean {

    private final UpdateOrderStatusUseCase useCase;

    private final SimpleMessageListenerContainer sufficientStockContainer;
    private final SimpleMessageListenerContainer insufficientStockContainer;

    private final Gson gson = new Gson();

    @Autowired
    public StockEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            InventoryRabbitMQConfig config,
            AmqpAdmin amqpAdmin,
            UpdateOrderStatusUseCase useCase) {

        this.useCase = useCase;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        Queue sufficientStockQueue = new Queue(config.sufficientStockQueueName(), false);
        Binding binding1 = BindingBuilder.bind(sufficientStockQueue)
                .to(exchange).with(config.sufficientStockRoutingKey());
        amqpAdmin.declareQueue(sufficientStockQueue);
        amqpAdmin.declareBinding(binding1);

        Queue insufficientStockQueue = new Queue(config.insufficientStockQueueName(), false);
        Binding binding2 = BindingBuilder.bind(insufficientStockQueue)
                .to(exchange).with(config.insufficientStockRoutingKey());
        amqpAdmin.declareQueue(insufficientStockQueue);
        amqpAdmin.declareBinding(binding2);

        sufficientStockContainer = new SimpleMessageListenerContainer();
        sufficientStockContainer.setConnectionFactory(connectionFactory);
        sufficientStockContainer.setQueueNames(sufficientStockQueue.getName());
        sufficientStockContainer.setMessageListener(message ->
                handleSufficientStockMessage(new String(message.getBody())));

        insufficientStockContainer = new SimpleMessageListenerContainer();
        insufficientStockContainer.setConnectionFactory(connectionFactory);
        insufficientStockContainer.setQueueNames(insufficientStockQueue.getName());
        insufficientStockContainer.setMessageListener(message ->
                handleInsufficientStockMessage(new String(message.getBody())));
    }

    @PostConstruct
    private void initialize() {

        if (sufficientStockContainer != null) {
            sufficientStockContainer.start();
        }

        if (insufficientStockContainer != null) {
            insufficientStockContainer.start();
        }
    }

    public void destroy() throws Exception {

        if (sufficientStockContainer != null) {
            sufficientStockContainer.stop();
        }

        if (insufficientStockContainer != null) {
            insufficientStockContainer.stop();
        }
    }

    private void handleSufficientStockMessage(String message) {
        try {
            System.out.printf("Received message: %s%n", message);
            SufficientStockForOrderEvent event = gson.fromJson(message, SufficientStockForOrderEvent.class);
            String reason = "Enough items in inventory to proceed with the order";
            useCase.handle(new UpdateOrderStatusCommand(event.orderId(), OrderStatus.INVENTORY_CHECK_OK, reason));
        } catch (Exception e) {
            System.err.printf("Error processing message: %s%n", e.getMessage());
        }
    }

    private void handleInsufficientStockMessage(String message) {
        try {
            System.out.printf("Received message: %s%n", message);
            InsufficientStockForOrderEvent event = gson.fromJson(message, InsufficientStockForOrderEvent.class);
            String reason = "Not enough items in inventory to proceed with the order";
            useCase.handle(new UpdateOrderStatusCommand(event.orderId(), OrderStatus.CANCELLED, reason));
        } catch (Exception e) {
            System.err.printf("Error processing message: %s%n", e.getMessage());
        }
    }
}
