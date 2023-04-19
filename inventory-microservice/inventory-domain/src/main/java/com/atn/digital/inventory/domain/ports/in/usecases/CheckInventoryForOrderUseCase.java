package com.atn.digital.inventory.domain.ports.in.usecases;

public interface CheckInventoryForOrderUseCase {
    boolean handle(CheckInventoryForOrderCommand command);
}
