package com.atn.digital.order.application.adapters.in.rabbitmq;

import com.atn.digital.order.application.adapters.config.rabbitmq.PaymentRabbitMQConfig;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentEventRabbitMQSender {

    private final RabbitTemplate rabbitTemplate;
    private final PaymentRabbitMQConfig config;
    private final Gson gson = new Gson();

    public void publish(PaymentCompletedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.paymentCompletedRoutingKey(),
                gson.toJson(event)
        );
    }

    public void publish(PaymentFailedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.paymentFailedRoutingKey(),
                gson.toJson(event)
        );
    }
}
