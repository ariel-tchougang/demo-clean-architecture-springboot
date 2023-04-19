package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.adapters.config.rabbitmq.ShipmentRabbitMQConfig;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ShipmentEventRabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final ShipmentRabbitMQConfig config;
    private final Gson gson = new Gson();

    public void publish(ShipmentCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.shipmentCreatedRoutingKey(),
                gson.toJson(event)
        );
    }

    public void publish(ShipmentStatusChangedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.shipmentStatusChangedRoutingKey(),
                gson.toJson(event)
        );
    }
}
