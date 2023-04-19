package com.atn.digital.user.adapters.in.web;

import com.atn.digital.user.domain.models.Order;
import com.atn.digital.user.domain.ports.in.queries.GetOrdersByUserIdQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GetOrdersByUserIdController {

    private final GetOrdersByUserIdQuery query;

    @GetMapping("/api/v1/users/{userId}/orders")
    public ResponseEntity<List<Order>> findByUserId(@PathVariable("userId") String userId) {
        List<Order> orders = query.findOrdersByUserId(userId);
        return ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
