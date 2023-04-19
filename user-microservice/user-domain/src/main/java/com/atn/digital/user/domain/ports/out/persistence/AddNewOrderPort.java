package com.atn.digital.user.domain.ports.out.persistence;

import com.atn.digital.user.domain.models.Order;

public interface AddNewOrderPort {
    void addNewOrder(Order order);
}
