package com.atn.digital.order.application.adapters.in.web;

import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderItem;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderCommand;
import com.atn.digital.order.domain.ports.in.usecases.CreateNewOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateNewOrderController {

    private final CreateNewOrderUseCase useCase;

    @PostMapping("/api/v1/orders")
    public ResponseEntity<OrderIdDto> createNewOrder(@RequestBody CreateNewOrderWeb newOrderWeb) {
        CreateNewOrderCommand newUserCommand = new CreateNewOrderCommand(
                newOrderWeb.getCustomerId(),
                newOrderWeb.getDeliveryAddress(),
                newOrderWeb.getItems().stream().map(item -> new OrderItem(
                        item.id(),
                        item.name(),
                        item.quantity(),
                        item.unitaryPrice())
                ).toList());
        OrderId orderId = useCase.handle(newUserCommand);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderIdDto(orderId.getId()));
    }
}
