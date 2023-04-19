package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.adapters.config.rabbitmq.ShipmentRabbitMQConfig;
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
public class ShipmentEventRabbitMQReceiver implements DisposableBean {

    private final UpdateOrderStatusUseCase useCase;

    private final SimpleMessageListenerContainer shipmentCreatedContainer;
    private final SimpleMessageListenerContainer shipmentStatusChangedContainer;

    private final Gson gson = new Gson();

    @Autowired
    public ShipmentEventRabbitMQReceiver(
            ConnectionFactory connectionFactory,
            ShipmentRabbitMQConfig config,
            AmqpAdmin amqpAdmin,
            UpdateOrderStatusUseCase useCase) {

        this.useCase = useCase;

        TopicExchange exchange = new TopicExchange(config.exchangeName());
        amqpAdmin.declareExchange(exchange);

        Queue shipmentCreatedQueue = new Queue(config.shipmentCreatedQueueName(), false);
        Binding binding1 = BindingBuilder.bind(shipmentCreatedQueue)
                .to(exchange).with(config.shipmentCreatedRoutingKey());
        amqpAdmin.declareQueue(shipmentCreatedQueue);
        amqpAdmin.declareBinding(binding1);

        Queue shipmentStatusChangedQueue = new Queue(config.shipmentStatusChangedQueueName(), false);
        Binding binding2 = BindingBuilder.bind(shipmentStatusChangedQueue)
                .to(exchange).with(config.shipmentStatusChangedRoutingKey());
        amqpAdmin.declareQueue(shipmentStatusChangedQueue);
        amqpAdmin.declareBinding(binding2);

        shipmentCreatedContainer = new SimpleMessageListenerContainer();
        shipmentCreatedContainer.setConnectionFactory(connectionFactory);
        shipmentCreatedContainer.setQueueNames(shipmentCreatedQueue.getName());
        shipmentCreatedContainer.setMessageListener(message ->
                handleShipmentCreatedMessage(new String(message.getBody())));

        shipmentStatusChangedContainer = new SimpleMessageListenerContainer();
        shipmentStatusChangedContainer.setConnectionFactory(connectionFactory);
        shipmentStatusChangedContainer.setQueueNames(shipmentStatusChangedQueue.getName());
        shipmentStatusChangedContainer.setMessageListener(message ->
                handleShipmentStatusChangedMessage(new String(message.getBody())));
    }

    @PostConstruct
    private void initialize() {

        if (shipmentCreatedContainer != null) {
            shipmentCreatedContainer.start();
        }

        if (shipmentStatusChangedContainer != null) {
            shipmentStatusChangedContainer.start();
        }
    }

    public void destroy() throws Exception {

        if (shipmentCreatedContainer != null) {
            shipmentCreatedContainer.stop();
        }

        if (shipmentStatusChangedContainer != null) {
            shipmentStatusChangedContainer.stop();
        }
    }

    private void handleShipmentCreatedMessage(String message) {
        try {
            System.out.printf("Received message: %s%n", message);
            ShipmentCreatedEvent event = gson.fromJson(message, ShipmentCreatedEvent.class);
            String reason = "Shipment processing in progress";
            useCase.handle(new UpdateOrderStatusCommand(event.orderId(), OrderStatus.PROCESSING_IN_PROGRESS, reason));
        } catch (Exception e) {
            System.err.printf("Error processing message: %s%n", e.getMessage());
        }
    }

    private void handleShipmentStatusChangedMessage(String message) {
        try {
            System.out.printf("Received message: %s%n", message);
            ShipmentStatusChangedEvent event = gson.fromJson(message, ShipmentStatusChangedEvent.class);
            OrderStatus status;
            String reason;

            switch (event.newStatus()) {
                case "ON_THE_WAY" -> {
                    status = OrderStatus.PENDING_DELIVERY;
                    reason = "Package has been shipped";
                }
                case "DELIVERED" -> {
                    status = OrderStatus.DELIVERED;
                    reason = "Package delivered";
                }
                case "LOST" -> {
                    status = OrderStatus.CANCELLED;
                    reason = "Package lost";
                }
                default -> {
                    status = OrderStatus.UNKNOWN;
                    reason = "Status unknown";
                }
            }

            useCase.handle(new UpdateOrderStatusCommand(event.orderId(), status, reason));
        } catch (Exception e) {
            System.err.printf("Error processing message: %s%n", e.getMessage());
        }
    }
}
