package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderCommand;
import com.atn.digital.user.domain.ports.in.usecases.AddNewOrderUseCase;
import com.atn.digital.user.domain.ports.out.persistence.AddNewOrderPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AddNewOrderService implements AddNewOrderUseCase {

    private final AddNewOrderPort repository;

    public void handle(AddNewOrderCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("Expected incoming command to be not null!");
        }

        repository.addNewOrder(new Order(
                command.getOrderId(),
                command.getUserId(),
                command.getDeliveryAddress(),
                command.getItems(),
                command.getAmount(),
                "CREATED",
                ""
        ));
    }
}
