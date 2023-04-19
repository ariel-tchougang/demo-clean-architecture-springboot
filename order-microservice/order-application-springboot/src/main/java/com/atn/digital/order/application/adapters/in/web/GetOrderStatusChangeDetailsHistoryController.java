package com.atn.digital.order.application.adapters.in.web;

import com.atn.digital.order.domain.models.Order;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatusChangeDetails;
import com.atn.digital.order.domain.models.OrderStatusDetails;
import com.atn.digital.order.domain.ports.in.queries.GetOrderStatusChangeDetailsHistoryQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetOrderStatusChangeDetailsHistoryController {

    private final GetOrderStatusChangeDetailsHistoryQuery query;

    @GetMapping("/api/v1/orders/status/history/{orderId}")
    public ResponseEntity<List<OrderStatusChangeDetails>> getStatusChangeDetailsHistory(
            @PathVariable("orderId") String orderId) {
        List<OrderStatusChangeDetails> history = query.getStatusChangeDetailsHistory(new OrderId(orderId));
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
}
