package com.atn.digital.payment.application.adapters.in.rabbitmq;

import com.atn.digital.payment.application.adapters.config.rabbitmq.PaymentRabbitMQConfig;
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
public class PaymentEventRabbitMQReceiver implements DisposableBean {

    private final ConnectionFactory connectionFactory;
    private final Queue paymentCompletedQueue;
    private final Queue paymentFailedQueue;
    private SimpleMessageListenerContainer paymentCompletedContainer;
    private SimpleMessageListenerContainer paymentFailedContainer;

    @Autowired
    public PaymentEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            PaymentRabbitMQConfig config,
            AmqpAdmin amqpAdmin) {

        this.connectionFactory = connectionFactory;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        paymentCompletedQueue = new Queue(config.paymentCompletedQueueName(), false);
        Binding binding1 = BindingBuilder.bind(paymentCompletedQueue)
                .to(exchange).with(config.paymentCompletedRoutingKey());
        amqpAdmin.declareQueue(paymentCompletedQueue);
        amqpAdmin.declareBinding(binding1);

        paymentFailedQueue = new Queue(config.paymentFailedQueueName(), false);
        Binding binding2 = BindingBuilder.bind(paymentFailedQueue)
                .to(exchange).with(config.paymentFailedRoutingKey());
        amqpAdmin.declareQueue(paymentFailedQueue);
        amqpAdmin.declareBinding(binding2);
    }

    public void destroy() throws Exception {

        if (paymentCompletedContainer != null) {
            paymentCompletedContainer.stop();
        }

        if (paymentFailedContainer != null) {
            paymentFailedContainer.stop();
        }
    }

    public SimpleMessageListenerContainer paymentCompletedContainer(MessageListener listener) {
        paymentCompletedContainer = new SimpleMessageListenerContainer();
        paymentCompletedContainer.setConnectionFactory(connectionFactory);
        paymentCompletedContainer.setQueueNames(paymentCompletedQueue.getName());
        paymentCompletedContainer.setMessageListener(listener);
        paymentCompletedContainer.start();
        return paymentCompletedContainer;
    }

    public SimpleMessageListenerContainer paymentFailedContainer(MessageListener listener) {
        paymentFailedContainer = new SimpleMessageListenerContainer();
        paymentFailedContainer.setConnectionFactory(connectionFactory);
        paymentFailedContainer.setQueueNames(paymentFailedQueue.getName());
        paymentFailedContainer.setMessageListener(listener);
        return paymentFailedContainer;
    }
}
