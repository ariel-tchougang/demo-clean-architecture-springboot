package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.adapters.config.rabbitmq.PaymentRabbitMQConfig;
import com.atn.digital.order.domain.models.OrderStatus;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusCommand;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
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
public class PaymentEventRabbitMQReceiver implements DisposableBean {

    private final UpdateOrderStatusUseCase useCase;
    private final SimpleMessageListenerContainer paymentCompletedContainer;
    private final SimpleMessageListenerContainer paymentFailedContainer;
    private final Gson gson = new Gson();

    @Autowired
    public PaymentEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            PaymentRabbitMQConfig config,
            AmqpAdmin amqpAdmin,
            UpdateOrderStatusUseCase useCase) {

        this.useCase = useCase;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        Queue paymentCompletedQueue = new Queue(config.paymentCompletedQueueName(), false);
        Binding binding1 = BindingBuilder.bind(paymentCompletedQueue)
                .to(exchange).with(config.paymentCompletedRoutingKey());
        amqpAdmin.declareQueue(paymentCompletedQueue);
        amqpAdmin.declareBinding(binding1);

        Queue paymentFailedQueue = new Queue(config.paymentFailedQueueName(), false);
        Binding binding2 = BindingBuilder.bind(paymentFailedQueue)
                .to(exchange).with(config.paymentFailedRoutingKey());
        amqpAdmin.declareQueue(paymentFailedQueue);
        amqpAdmin.declareBinding(binding2);

        paymentCompletedContainer = new SimpleMessageListenerContainer();
        paymentCompletedContainer.setConnectionFactory(connectionFactory);
        paymentCompletedContainer.setQueueNames(paymentCompletedQueue.getName());
        paymentCompletedContainer.setMessageListener(message ->
                handlePaymentCompletedMessage(new String(message.getBody())));

        paymentFailedContainer = new SimpleMessageListenerContainer();
        paymentFailedContainer.setConnectionFactory(connectionFactory);
        paymentFailedContainer.setQueueNames(paymentFailedQueue.getName());
        paymentFailedContainer.setMessageListener(message ->
                handlePaymentFailedMessage(new String(message.getBody())));
    }

    @PostConstruct
    private void initialize() {

        if (paymentCompletedContainer != null) {
            paymentCompletedContainer.start();
        }

        if (paymentFailedContainer != null) {
            paymentFailedContainer.start();
        }
    }

    public void destroy() throws Exception {

        if (paymentCompletedContainer != null) {
            paymentCompletedContainer.stop();
        }

        if (paymentFailedContainer != null) {
            paymentFailedContainer.stop();
        }
    }

    private void handlePaymentCompletedMessage(String message) {
        try {
            System.out.printf("Received message: %s%n", message);
            PaymentCompletedEvent event = gson.fromJson(message, PaymentCompletedEvent.class);
            String reason = "Payment completed successfully";
            useCase.handle(new UpdateOrderStatusCommand(event.orderId(), OrderStatus.PAYMENT_COMPLETED, reason));
        } catch (Exception e) {
            System.err.printf("Error processing message: %s%n", e.getMessage());
        }
    }

    private void handlePaymentFailedMessage(String message) {
        try {
            System.out.printf("Received message: %s%n", message);
            PaymentFailedEvent event = gson.fromJson(message, PaymentFailedEvent.class);
            useCase.handle(new UpdateOrderStatusCommand(event.orderId(), OrderStatus.CANCELLED, event.errorMessage()));
        } catch (Exception e) {
            System.err.printf("Error processing message: %s%n", e.getMessage());
        }
    }
}
