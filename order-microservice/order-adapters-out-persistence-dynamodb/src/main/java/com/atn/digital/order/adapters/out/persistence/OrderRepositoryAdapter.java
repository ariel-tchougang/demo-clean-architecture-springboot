package com.atn.digital.order.adapters.out.persistence;

import com.atn.digital.order.domain.ports.out.persistence.CreateNewOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.LoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.UpdateOrderPort;

public abstract class OrderRepositoryAdapter implements CreateNewOrderPort, LoadOrderPort, UpdateOrderPort { }
