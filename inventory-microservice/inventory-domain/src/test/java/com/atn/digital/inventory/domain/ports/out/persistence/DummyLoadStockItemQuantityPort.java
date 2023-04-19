package com.atn.digital.inventory.domain.ports.out.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyLoadStockItemQuantityPort implements LoadStockItemQuantityPort {
    public Map<String, Integer> loadQuantities(List<String> ids) {
        final Map<String, Integer> map = new HashMap<>();
        ids.forEach(id -> map.put(id, 5));
        return map;
    }
}
