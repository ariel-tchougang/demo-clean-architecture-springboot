package com.atn.digital.order.application.config;

import com.atn.digital.order.adapters.out.persistence.OrderRepositoryAdapter;
import com.atn.digital.order.adapters.out.persistence.dynamodb.DynamoDbOrderRepository;
import com.atn.digital.order.application.adapters.config.rabbitmq.OrderRabbitMQConfig;
import com.atn.digital.order.application.adapters.out.notifications.OrderEventPublisherAdapter;
import com.atn.digital.order.application.adapters.out.notifications.rabbitmq.OrderEventRabbitMQPublisher;
import com.atn.digital.order.domain.ports.in.queries.GetOrderCurrentStatusDetailsQuery;
import com.atn.digital.order.domain.ports.in.queries.GetOrderStatusChangeDetailsHistoryQuery;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderUseCase;
import com.atn.digital.order.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.atn.digital.order.domain.services.CreateNewOrderService;
import com.atn.digital.order.domain.services.GetOrderCurrentStatusDetailsService;
import com.atn.digital.order.domain.services.GetOrderStatusChangeDetailsHistoryService;
import com.atn.digital.order.domain.services.UpdateOrderStatusService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class OrderDomainConfig {

    @Bean
    public OrderRepositoryAdapter orderRepositoryAdapter(DynamoDbClient client) {
        return new DynamoDbOrderRepository(client);
    }

    @Bean
    public OrderEventPublisherAdapter orderEventPublisherAdapter(
            RabbitTemplate rabbitTemplate,
            OrderRabbitMQConfig config) {
        return new OrderEventRabbitMQPublisher(rabbitTemplate, config);
    }

    @Bean
    public CreateNewOrderUseCase createNewOrderUseCase(
            OrderRepositoryAdapter repositoryAdapter,
            OrderEventPublisherAdapter publisher) {
        return new CreateNewOrderService(repositoryAdapter, publisher);
    }

    @Bean
    public GetOrderCurrentStatusDetailsQuery getOrderCurrentStatusDetailsQuery(
            OrderRepositoryAdapter repositoryAdapter) {
        return new GetOrderCurrentStatusDetailsService(repositoryAdapter);
    }

    @Bean
    public GetOrderStatusChangeDetailsHistoryQuery getOrderStatusChangeDetailsHistoryQuery(
            OrderRepositoryAdapter repositoryAdapter) {
        return new GetOrderStatusChangeDetailsHistoryService(repositoryAdapter);
    }

    @Bean
    public UpdateOrderStatusUseCase updateOrderStatusUseCase(
            OrderRepositoryAdapter repositoryAdapter,
            OrderEventPublisherAdapter publisher) {
        return new UpdateOrderStatusService(repositoryAdapter, repositoryAdapter, publisher);
    }
}
