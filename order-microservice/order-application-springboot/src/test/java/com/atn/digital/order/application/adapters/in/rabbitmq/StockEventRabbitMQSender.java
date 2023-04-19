package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.adapters.config.rabbitmq.InventoryRabbitMQConfig;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StockEventRabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final InventoryRabbitMQConfig config;
    private final Gson gson = new Gson();

    public void publish(SufficientStockForOrderEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.sufficientStockRoutingKey(),
                gson.toJson(event)
        );
    }

    public void publish(InsufficientStockForOrderEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.insufficientStockRoutingKey(),
                gson.toJson(event)
        );
    }
}
