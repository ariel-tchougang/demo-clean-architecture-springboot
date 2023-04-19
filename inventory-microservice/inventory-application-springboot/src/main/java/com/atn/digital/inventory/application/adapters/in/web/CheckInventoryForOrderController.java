package com.atn.digital.inventory.application.adapters.in.web;

import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderCommand;
import com.atn.digital.inventory.domain.ports.in.usecases.CheckInventoryForOrderUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckInventoryForOrderController {

    private final CheckInventoryForOrderUseCase useCase;

    @PostMapping("/api/v1/inventory/check")
    public ResponseEntity<Boolean> checkInventoryForOrder(@RequestBody CheckInventoryForOrderWeb dataToCheck) {
        CheckInventoryForOrderCommand command = new CheckInventoryForOrderCommand(
                dataToCheck.orderId(),
                dataToCheck.items());
        return ResponseEntity.status(HttpStatus.OK).body(useCase.handle(command));
    }
}
