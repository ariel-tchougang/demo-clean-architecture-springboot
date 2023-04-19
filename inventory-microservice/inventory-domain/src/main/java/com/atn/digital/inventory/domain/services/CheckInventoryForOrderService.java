package com.atn.digital.inventory.domain.services;

import com.atn.digital.inventory.domain.models.OrderItemData;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderCommand;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderUseCase;
import com.atn.digital.inventory.domain.ports.out.notifications.InsufficientStockForOrderEvent;
import com.atn.digital.inventory.domain.ports.out.notifications.SufficientStockPublisher;
import com.atn.digital.inventory.domain.ports.out.notifications.InsufficientStockPublisher;
import com.atn.digital.inventory.domain.ports.out.notifications.SufficientStockForOrderEvent;
import com.atn.digital.inventory.domain.ports.out.persistence.LoadStockItemQuantityPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class CheckInventoryForOrderService implements CheckInventoryForOrderUseCase {

    private final LoadStockItemQuantityPort port;
    private final SufficientStockPublisher sufficientStockPublisher;
    private final InsufficientStockPublisher insufficientStockPublisher;

    public boolean handle(CheckInventoryForOrderCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("command must not be null!");
        }

        final Map<String, Integer> map = port.loadQuantities(
                command.getItems().stream().map(item -> item.id()).toList());

        List<OrderItemData> insufficientItems = command.getItems().stream()
                .filter(item -> item.quantity() > map.get(item.id()))
                .toList();

        boolean enoughStock = insufficientItems.isEmpty();
        if (enoughStock) {
            sufficientStockPublisher.publish(
                    new SufficientStockForOrderEvent(command.getOrderId()));
        } else {
            insufficientStockPublisher.publish(
                    new InsufficientStockForOrderEvent(command.getOrderId(), insufficientItems));
        }

        return enoughStock;
    }
}
