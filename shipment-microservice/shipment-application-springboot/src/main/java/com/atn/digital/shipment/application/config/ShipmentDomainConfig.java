package com.atn.digital.shipment.application.config;

import com.atn.digital.shipment.adapters.out.persistence.ShipmentRepositoryAdapter;
import com.atn.digital.shipment.adapters.out.persistence.dynamodb.DynamoDbShipmentRepository;
import com.atn.digital.shipment.application.adapters.config.rabbitmq.ShipmentRabbitMQConfig;
import com.atn.digital.shipment.application.adapters.out.notifications.ShipmentEventPublisherAdapter;
import com.atn.digital.shipment.application.adapters.out.notifications.rabbitmq.ShipmentEventRabbitMQPublisher;
import com.atn.digital.shipment.domain.ports.in.queries.GetShipmentCurrentStatusDetailsQuery;
import com.atn.digital.shipment.domain.ports.in.queries.GetShipmentStatusChangeDetailsHistoryQuery;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentUseCase;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusUseCase;
import com.atn.digital.shipment.domain.services.CreateNewShipmentService;
import com.atn.digital.shipment.domain.services.GetShipmentCurrentStatusDetailsService;
import com.atn.digital.shipment.domain.services.GetShipmentStatusChangeDetailsHistoryService;
import com.atn.digital.shipment.domain.services.UpdateShipmentStatusService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class ShipmentDomainConfig {

    @Bean
    public ShipmentRepositoryAdapter orderRepositoryAdapter(DynamoDbClient client) {
        return new DynamoDbShipmentRepository(client);
    }

    @Bean
    public ShipmentEventPublisherAdapter shipmentEventPublisherAdapter(
            RabbitTemplate rabbitTemplate,
            ShipmentRabbitMQConfig config) {
        return new ShipmentEventRabbitMQPublisher(rabbitTemplate, config);
    }

    @Bean
    public CreateNewShipmentUseCase createNewShipmentUseCase(ShipmentRepositoryAdapter repositoryAdapter,
                                                             ShipmentEventPublisherAdapter publisher) {
        return new CreateNewShipmentService(repositoryAdapter, publisher);
    }

    @Bean
    public GetShipmentCurrentStatusDetailsQuery getShipmentCurrentStatusDetailsQuery(
            ShipmentRepositoryAdapter repositoryAdapter) {
        return new GetShipmentCurrentStatusDetailsService(repositoryAdapter);
    }

    @Bean
    public GetShipmentStatusChangeDetailsHistoryQuery getShipmentStatusChangeDetailsHistoryQuery(
            ShipmentRepositoryAdapter repositoryAdapter) {
        return new GetShipmentStatusChangeDetailsHistoryService(repositoryAdapter);
    }

    @Bean
    public UpdateShipmentStatusUseCase updateShipmentStatusUseCase(
            ShipmentRepositoryAdapter repositoryAdapter, ShipmentEventPublisherAdapter publisher) {
        return new UpdateShipmentStatusService(repositoryAdapter, repositoryAdapter, publisher);
    }
}
