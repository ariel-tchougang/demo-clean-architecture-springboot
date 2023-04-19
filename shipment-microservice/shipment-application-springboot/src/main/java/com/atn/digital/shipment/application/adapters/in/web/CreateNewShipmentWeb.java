package com.atn.digital.shipment.application.adapters.in.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateNewShipmentWeb {
    private String orderId;
    private String deliveryAddress;
    private List<NewShipmentItemWeb> items;

    record NewShipmentItemWeb(String id, String name, Integer quantity) {}
}
