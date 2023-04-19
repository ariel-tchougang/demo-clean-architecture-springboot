package com.atn.digital.user.adapters.in.rabbitmq;

import com.atn.digital.user.adapters.config.rabbitmq.OrderRabbitMQConfig;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderEventRabbitMQSender {

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
                config.orderStatusChangedRoutingKey(),
                gson.toJson(event)
        );
    }
}
