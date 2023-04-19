package com.atn.digital.shipment.application.adapters.out.notifications.rabbitmq;

import com.atn.digital.shipment.application.adapters.config.rabbitmq.ShipmentRabbitMQConfig;
import com.atn.digital.shipment.application.adapters.out.notifications.ShipmentEventPublisherAdapter;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentCreatedEvent;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentStatusChangedEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class ShipmentEventRabbitMQPublisher extends ShipmentEventPublisherAdapter {

    private final RabbitTemplate rabbitTemplate;
    private final ShipmentRabbitMQConfig config;
    private final Gson gson = new Gson();

    public void publish(ShipmentCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.shipmentCreatedRoutingKey(), gson.toJson(event)
        );
    }

    public void publish(ShipmentStatusChangedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.shipmentStatusChangedRoutingKey(), gson.toJson(event)
        );
    }
}
