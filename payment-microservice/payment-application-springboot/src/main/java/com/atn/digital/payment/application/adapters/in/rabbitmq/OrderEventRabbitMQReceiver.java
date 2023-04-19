package com.atn.digital.payment.application.adapters.in.rabbitmq;

import com.atn.digital.payment.application.adapters.config.rabbitmq.OrderRabbitMQConfig;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentCommand;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentUseCase;
import com.atn.digital.payment.domain.ports.out.processing.PaymentFailureException;
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

import java.math.BigDecimal;
import java.util.List;

@Component
public class OrderEventRabbitMQReceiver implements DisposableBean {

    private final ProcessPaymentUseCase useCase;
    private final SimpleMessageListenerContainer orderStatusChangedContainer;

    private final Gson gson = new Gson();

    @Autowired
    public OrderEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            OrderRabbitMQConfig config,
            AmqpAdmin amqpAdmin,
            ProcessPaymentUseCase useCase) {

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

        if ("INVENTORY_CHECK_OK".equals(event.newStatus())) {
            try {
                useCase.handle(
                        new ProcessPaymentCommand(
                                event.orderId(),
                                event.deliveryAddress(),
                                "Incoming order",
                                getAmount(event.items())
                        ));
            } catch (PaymentFailureException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private BigDecimal getAmount(List<OrderItemData> items) {
        return items.isEmpty() ? BigDecimal.ZERO :
                items.stream().map(item -> item.unitaryPrice().multiply(BigDecimal.valueOf(item.quantity())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
