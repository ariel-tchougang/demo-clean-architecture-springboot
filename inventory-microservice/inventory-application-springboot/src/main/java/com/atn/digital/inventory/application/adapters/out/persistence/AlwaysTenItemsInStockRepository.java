package com.atn.digital.inventory.application.adapters.out.persistence;

import com.atn.digital.inventory.domain.ports.out.persistence.LoadStockItemQuantityPort;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlwaysTenItemsInStockRepository implements LoadStockItemQuantityPort {
    public Map<String, Integer> loadQuantities(List<String> ids) {
        final Map<String, Integer> map = new HashMap<>();
        ids.forEach(id -> map.put(id, 10));
        return map;
    }
}
