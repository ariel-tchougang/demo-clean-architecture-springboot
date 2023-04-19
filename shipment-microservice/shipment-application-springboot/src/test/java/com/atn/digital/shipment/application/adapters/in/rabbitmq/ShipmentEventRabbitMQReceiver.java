package com.atn.digital.shipment.application.adapters.in.rabbitmq;

import com.atn.digital.shipment.application.adapters.config.rabbitmq.ShipmentRabbitMQConfig;
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
public class ShipmentEventRabbitMQReceiver implements DisposableBean {

    private final ConnectionFactory connectionFactory;
    private final Queue shipmentCreatedQueue;
    private final Queue shipmentStatusChangedQueue;
    private SimpleMessageListenerContainer shipmentCreatedContainer;
    private SimpleMessageListenerContainer shipmentStatusChangedContainer;

    @Autowired
    public ShipmentEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            ShipmentRabbitMQConfig config,
            AmqpAdmin amqpAdmin) {

        this.connectionFactory = connectionFactory;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        shipmentCreatedQueue = new Queue(config.shipmentCreatedQueueName(), false);
        Binding binding1 = BindingBuilder.bind(shipmentCreatedQueue)
                .to(exchange).with(config.shipmentCreatedRoutingKey());
        amqpAdmin.declareQueue(shipmentCreatedQueue);
        amqpAdmin.declareBinding(binding1);

        shipmentStatusChangedQueue = new Queue(config.shipmentStatusChangedQueueName(), false);
        Binding binding2 = BindingBuilder.bind(shipmentStatusChangedQueue)
                .to(exchange).with(config.shipmentStatusChangedRoutingKey());
        amqpAdmin.declareQueue(shipmentStatusChangedQueue);
        amqpAdmin.declareBinding(binding2);
    }

    public void destroy() throws Exception {

        if (shipmentCreatedContainer != null) {
            shipmentCreatedContainer.stop();
        }

        if (shipmentStatusChangedContainer != null) {
            shipmentStatusChangedContainer.stop();
        }
    }

    public SimpleMessageListenerContainer shipmentCreatedContainer(MessageListener listener) {
        shipmentCreatedContainer = new SimpleMessageListenerContainer();
        shipmentCreatedContainer.setConnectionFactory(connectionFactory);
        shipmentCreatedContainer.setQueueNames(shipmentCreatedQueue.getName());
        shipmentCreatedContainer.setMessageListener(listener);
        return shipmentCreatedContainer;
    }

    public SimpleMessageListenerContainer shipmentStatusChangedContainer(MessageListener listener) {
        shipmentStatusChangedContainer = new SimpleMessageListenerContainer();
        shipmentStatusChangedContainer.setConnectionFactory(connectionFactory);
        shipmentStatusChangedContainer.setQueueNames(shipmentStatusChangedQueue.getName());
        shipmentStatusChangedContainer.setMessageListener(listener);
        return shipmentStatusChangedContainer;
    }
}
