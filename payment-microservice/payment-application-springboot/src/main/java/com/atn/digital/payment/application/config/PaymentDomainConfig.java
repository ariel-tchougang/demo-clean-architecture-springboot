package com.atn.digital.payment.application.config;

import com.atn.digital.payment.adapters.out.persistence.PaymentRepositoryAdapter;
import com.atn.digital.payment.adapters.out.persistence.dynamodb.DynamoDbPaymentRepository;
import com.atn.digital.payment.application.adapters.config.rabbitmq.PaymentRabbitMQConfig;
import com.atn.digital.payment.application.adapters.out.notifications.PaymentEventPublisherAdapter;
import com.atn.digital.payment.application.adapters.out.notifications.rabbitmq.PaymentEventRabbitMQPublisher;
import com.atn.digital.payment.domain.ports.in.ProcessPaymentUseCase;
import com.atn.digital.payment.domain.ports.out.processing.DummyPaymentOperator;
import com.atn.digital.payment.domain.ports.out.processing.PaymentOperator;
import com.atn.digital.payment.domain.services.ProcessPaymentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class PaymentDomainConfig {

    @Bean
    public PaymentRepositoryAdapter paymentRepositoryAdapter(DynamoDbClient client) {
        return new DynamoDbPaymentRepository(client);
    }

    @Bean
    public PaymentEventPublisherAdapter paymentEventPublisherAdapter(
            RabbitTemplate rabbitTemplate,
            PaymentRabbitMQConfig config) {
        return new PaymentEventRabbitMQPublisher(rabbitTemplate, config);
    }

    @Bean
    public PaymentOperator paymentOperator() {
        return new DummyPaymentOperator();
    }

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(
            PaymentOperator paymentOperator,
            PaymentRepositoryAdapter repository,
            PaymentEventPublisherAdapter publisher) {
        return new ProcessPaymentService(paymentOperator, repository, publisher, publisher);
    }
}
