package com.atn.digital.inventory.application.adapters.out.notifications.rabbitmq;

import com.atn.digital.inventory.application.adapters.out.notifications.StockEventPublisherAdapter;
import com.atn.digital.inventory.application.adapters.config.rabbitmq.InventoryRabbitMQConfig;
import com.atn.digital.inventory.domain.ports.out.notifications.InsufficientStockForOrderEvent;
import com.atn.digital.inventory.domain.ports.out.notifications.SufficientStockForOrderEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class StockEventRabbitMQPublisher extends StockEventPublisherAdapter {

    private final RabbitTemplate rabbitTemplate;
    private final InventoryRabbitMQConfig config;
    private final Gson gson = new Gson();

    public void publish(SufficientStockForOrderEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.sufficientStockRoutingKey(), gson.toJson(event)
        );
    }

    public void publish(InsufficientStockForOrderEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.insufficientStockRoutingKey(), gson.toJson(event)
        );
    }
}
