package com.atn.digital.order.application.adapters.in.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateNewOrderWeb {
    private String customerId;
    private String deliveryAddress;
    private List<NewOrderItemWeb> items;

    record NewOrderItemWeb(String id, String name, Integer quantity, BigDecimal unitaryPrice) {}
}
