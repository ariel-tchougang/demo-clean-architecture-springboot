package com.atn.digital.payment.application.adapters.out.notifications.rabbitmq;

import com.atn.digital.payment.application.adapters.config.rabbitmq.PaymentRabbitMQConfig;
import com.atn.digital.payment.application.adapters.out.notifications.PaymentEventPublisherAdapter;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentCompletedEvent;
import com.atn.digital.payment.domain.ports.out.notifications.PaymentFailedEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RequiredArgsConstructor
public class PaymentEventRabbitMQPublisher extends PaymentEventPublisherAdapter {

    private final RabbitTemplate rabbitTemplate;
    private final PaymentRabbitMQConfig config;
    private final Gson gson = new Gson();

    public void publish(PaymentCompletedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.paymentCompletedRoutingKey(), gson.toJson(event)
        );
    }

    public void publish(PaymentFailedEvent event) {
        rabbitTemplate.convertAndSend(
                config.exchangeName(),
                config.paymentFailedRoutingKey(), gson.toJson(event)
        );
    }
}
