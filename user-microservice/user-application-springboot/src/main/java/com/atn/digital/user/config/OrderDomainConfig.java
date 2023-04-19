package com.atn.digital.user.config;

import com.atn.digital.user.adapters.out.persistence.OrderRepositoryAdapter;
import com.atn.digital.user.adapters.out.persistence.UserRepositoryAdapter;
import com.atn.digital.user.adapters.out.persistence.dynamodb.DynamoDbOrderRepository;
import com.atn.digital.user.adapters.out.persistence.dynamodb.DynamoDbUserRepository;
import com.atn.digital.user.domain.ports.in.queries.GetOrdersByUserIdQuery;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderUseCase;
import com.atn.digital.user.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.atn.digital.user.domain.services.AddNewOrderService;
import com.atn.digital.user.domain.services.GetOrdersByUserIdService;
import com.atn.digital.user.domain.services.UpdateOrderStatusService;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class OrderDomainConfig {

    @Bean
    public OrderRepositoryAdapter orderRepositoryAdapter(
            DynamoDbClient client,
            UserRepositoryAdapter userRepositoryAdapter) {
        return new DynamoDbOrderRepository((DynamoDbUserRepository)userRepositoryAdapter, client);
    }

    @Bean
    public AddNewOrderUseCase addNewOrderUseCase(OrderRepositoryAdapter repositoryAdapter) {
        return new AddNewOrderService(repositoryAdapter);
    }

    @Bean
    public UpdateOrderStatusUseCase updateOrderStatusUseCase(OrderRepositoryAdapter repositoryAdapter) {
        return new UpdateOrderStatusService(repositoryAdapter);
    }

    @Bean
    public GetOrdersByUserIdQuery getOrdersByUserIdQuery(OrderRepositoryAdapter repositoryAdapter) {
        return new GetOrdersByUserIdService(repositoryAdapter);
    }
}
