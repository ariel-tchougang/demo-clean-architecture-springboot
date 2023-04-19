package com.atn.digital.user.adapters.out.persistence;

import com.atn.digital.user.domain.ports.out.persistence.AddNewOrderPort;
import com.atn.digital.user.domain.ports.out.persistence.GetOrdersByUserIdPort;
import com.atn.digital.user.domain.ports.out.persistence.UpdateOrderStatusPort;

public abstract class OrderRepositoryAdapter implements AddNewOrderPort, UpdateOrderStatusPort, GetOrdersByUserIdPort { }
