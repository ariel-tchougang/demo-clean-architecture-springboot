package com.atn.digital.order.application.adapters.in.web;

import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatusDetails;
import com.atn.digital.order.domain.ports.in.queries.GetOrderCurrentStatusDetailsQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class GetOrderCurrentStatusDetailsController {

    private final GetOrderCurrentStatusDetailsQuery query;

    @GetMapping("/api/v1/orders/status/{orderId}")
    public ResponseEntity<OrderStatusDetails> getStatusDetails(@PathVariable("orderId") String orderId) {
        Optional<OrderStatusDetails> details = query.getStatusDetails(new OrderId(orderId));
        return ResponseEntity.of(details);
    }
}
