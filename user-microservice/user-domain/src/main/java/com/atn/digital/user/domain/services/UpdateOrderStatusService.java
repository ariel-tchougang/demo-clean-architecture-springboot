package com.atn.digital.user.domain.services;

import com.atn.digital.user.domain.ports.in.usecases.UpdateOrderStatusCommand;
import com.atn.digital.user.domain.ports.in.usecases.UpdateOrderStatusUseCase;
import com.atn.digital.user.domain.ports.out.persistence.UpdateOrderStatusPort;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateOrderStatusService implements UpdateOrderStatusUseCase {

    private final UpdateOrderStatusPort repository;

    public void handle(UpdateOrderStatusCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("Expected incoming command to be not null!");
        }

        repository.updateStatus(command.getUserId(), command.getOrderId(), command.getNewStatus(), command.getReason());
    }
}
