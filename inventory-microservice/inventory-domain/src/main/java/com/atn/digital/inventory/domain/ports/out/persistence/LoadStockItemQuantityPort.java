package com.atn.digital.inventory.domain.ports.out.persistence;

import java.util.List;
import java.util.Map;

public interface LoadStockItemQuantityPort {
    Map<String, Integer> loadQuantities(List<String> ids);
}
