package com.atn.digital.inventory.application.adapters.in.rabbitmq;

import com.atn.digital.inventory.application.adapters.config.rabbitmq.InventoryRabbitMQConfig;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StockEventRabbitMQReceiver {

    private final ConnectionFactory connectionFactory;
    private final Queue sufficientStockQueue;
    private final Queue insufficientStockQueue;

    @Autowired
    public StockEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            InventoryRabbitMQConfig config,
            AmqpAdmin amqpAdmin) {

        this.connectionFactory = connectionFactory;
        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        sufficientStockQueue = new Queue(config.sufficientStockQueueName(), false);
        Binding binding1 = BindingBuilder.bind(sufficientStockQueue)
                .to(exchange).with(config.sufficientStockRoutingKey());
        amqpAdmin.declareQueue(sufficientStockQueue);
        amqpAdmin.declareBinding(binding1);

        insufficientStockQueue = new Queue(config.insufficientStockQueueName(), false);
        Binding binding2 = BindingBuilder.bind(insufficientStockQueue)
                .to(exchange).with(config.insufficientStockRoutingKey());
        amqpAdmin.declareQueue(insufficientStockQueue);
        amqpAdmin.declareBinding(binding2);
    }

    public SimpleMessageListenerContainer container(MessageListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(sufficientStockQueue.getName(), insufficientStockQueue.getName());
        container.setMessageListener(listener);
        return container;
    }
}
