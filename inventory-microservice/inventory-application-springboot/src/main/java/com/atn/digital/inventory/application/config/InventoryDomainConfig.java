package com.atn.digital.inventory.application.config;


import com.atn.digital.inventory.application.adapters.config.rabbitmq.InventoryRabbitMQConfig;
import com.atn.digital.inventory.application.adapters.out.notifications.StockEventPublisherAdapter;
import com.atn.digital.inventory.application.adapters.out.notifications.rabbitmq.StockEventRabbitMQPublisher;
import com.atn.digital.inventory.application.adapters.out.persistence.AlwaysTenItemsInStockRepository;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderUseCase;
import com.atn.digital.inventory.domain.ports.out.persistence.LoadStockItemQuantityPort;
import com.atn.digital.inventory.domain.services.CheckInventoryForOrderService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InventoryDomainConfig {

    @Bean
    public StockEventPublisherAdapter stockEventPublisherAdapter(
            RabbitTemplate rabbitTemplate,
            InventoryRabbitMQConfig config) {
        return new StockEventRabbitMQPublisher(rabbitTemplate, config);
    }

    @Bean
    public LoadStockItemQuantityPort loadStockItemQuantityPort() {
        return new AlwaysTenItemsInStockRepository();
    }

    @Bean
    public CheckInventoryForOrderUseCase checkInventoryForOrderUseCase(
            LoadStockItemQuantityPort repository,
            StockEventPublisherAdapter publisher) {
        return new CheckInventoryForOrderService(repository, publisher, publisher);
    }
}
