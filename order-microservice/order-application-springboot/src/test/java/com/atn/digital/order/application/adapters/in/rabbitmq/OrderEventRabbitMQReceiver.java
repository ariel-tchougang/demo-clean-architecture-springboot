package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.adapters.config.rabbitmq.OrderRabbitMQConfig;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderEventRabbitMQReceiver implements DisposableBean {

    private final ConnectionFactory connectionFactory;
    private final Queue orderCreatedQueue;
    private final Queue orderStatusChangedQueue;
    private SimpleMessageListenerContainer orderCreatedContainer;
    private SimpleMessageListenerContainer orderStatusChangedContainer;

    @Autowired
    public OrderEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            OrderRabbitMQConfig config,
            AmqpAdmin amqpAdmin) {

        this.connectionFactory = connectionFactory;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        orderCreatedQueue = new Queue(config.orderCreatedQueueName(), false);
        Binding binding1 = BindingBuilder.bind(orderCreatedQueue)
                .to(exchange).with(config.orderCreatedRoutingKey());
        amqpAdmin.declareQueue(orderCreatedQueue);
        amqpAdmin.declareBinding(binding1);

        orderStatusChangedQueue = new Queue(config.orderStatusChangedQueueName(), false);
        Binding binding2 = BindingBuilder.bind(orderStatusChangedQueue)
                .to(exchange).with(config.orderStatusChangedRoutingKey());
        amqpAdmin.declareQueue(orderStatusChangedQueue);
        amqpAdmin.declareBinding(binding2);
    }

    public void destroy() throws Exception {

        if (orderCreatedContainer != null) {
            orderCreatedContainer.stop();
        }

        if (orderStatusChangedContainer != null) {
            orderStatusChangedContainer.stop();
        }
    }

    public SimpleMessageListenerContainer orderCreatedContainer(MessageListener listener) {
        orderCreatedContainer = new SimpleMessageListenerContainer();
        orderCreatedContainer.setConnectionFactory(connectionFactory);
        orderCreatedContainer.setQueueNames(orderCreatedQueue.getName());
        orderCreatedContainer.setMessageListener(listener);
        return orderCreatedContainer;
    }

    public SimpleMessageListenerContainer orderStatusChangedContainer(MessageListener listener) {
        orderStatusChangedContainer = new SimpleMessageListenerContainer();
        orderStatusChangedContainer.setConnectionFactory(connectionFactory);
        orderStatusChangedContainer.setQueueNames(orderStatusChangedQueue.getName());
        orderStatusChangedContainer.setMessageListener(listener);
        return orderStatusChangedContainer;
    }
}
