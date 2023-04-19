package com.atn.digital.order.application.adapters.out.notifications.rabbitmq;

import com.atn.digital.order.application.adapters.config.rabbitmq.OrderRabbitMQConfig;
import com.atn.digital.order.application.adapters.out.notifications.OrderEventPublisherAdapter;
import com.atn.digital.order.domain.ports.out.notifications.OrderCreatedEvent;
import com.atn.digital.order.domain.ports.out.notifications.OrderStatusChangedEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class OrderEventRabbitMQPublisher extends OrderEventPublisherAdapter {

    private final RabbitTemplate rabbitTemplate;
    private final OrderRabbitMQConfig config;
    private final Gson gson = new Gson();

    public void publish(OrderCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.orderCreatedRoutingKey(), gson.toJson(event)
        );
    }

    public void publish(OrderStatusChangedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.orderStatusChangedRoutingKey(), gson.toJson(event)
        );
    }
}
