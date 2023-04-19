package com.atn.digital.order.domain.services;

import com.atn.digital.order.domain.exceptions.OrderNotFoundException;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatusChangeDetails;
import com.atn.digital.order.domain.ports.in.queries.GetOrderStatusChangeDetailsHistoryQuery;
import com.atn.digital.order.domain.ports.out.persistence.FailedLoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.FakeLoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.LoadOrderPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GetOrderStatusChangeDetailsHistoryServiceTest {

    private final LoadOrderPort port = new FakeLoadOrderPort();
    private final GetOrderStatusChangeDetailsHistoryQuery query = new GetOrderStatusChangeDetailsHistoryService(port);
    private final LoadOrderPort failedLoader = new FailedLoadOrderPort();
    private final GetOrderStatusChangeDetailsHistoryQuery failedQuery =
            new GetOrderStatusChangeDetailsHistoryService(failedLoader);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> query.getStatusChangeDetailsHistory(null));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderIdDoesntExists() {
        Assertions.assertThrows(OrderNotFoundException.class,
                () -> failedQuery.getStatusChangeDetailsHistory(new OrderId("unknown_id")));
    }

    @Test
    void shouldReturnStatusChangeDetailsHistory() {
        List<OrderStatusChangeDetails> history = query.getStatusChangeDetailsHistory(new OrderId("id"));
        Assertions.assertNotNull(history);
        Assertions.assertFalse(history.isEmpty());
    }
}
