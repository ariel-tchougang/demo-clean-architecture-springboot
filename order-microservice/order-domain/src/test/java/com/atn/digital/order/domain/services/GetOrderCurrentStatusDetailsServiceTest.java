package com.atn.digital.order.domain.services;

import com.atn.digital.order.domain.exceptions.OrderNotFoundException;
import com.atn.digital.order.domain.models.Order.OrderId;
import com.atn.digital.order.domain.models.OrderStatusDetails;
import com.atn.digital.order.domain.ports.in.queries.GetOrderCurrentStatusDetailsQuery;
import com.atn.digital.order.domain.ports.out.persistence.FailedLoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.FakeLoadOrderPort;
import com.atn.digital.order.domain.ports.out.persistence.LoadOrderPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class GetOrderCurrentStatusDetailsServiceTest {

    private final LoadOrderPort port = new FakeLoadOrderPort();
    private final GetOrderCurrentStatusDetailsQuery query = new GetOrderCurrentStatusDetailsService(port);
    private final LoadOrderPort failedLoader = new FailedLoadOrderPort();
    private final GetOrderCurrentStatusDetailsQuery failedQuery = new GetOrderCurrentStatusDetailsService(failedLoader);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> query.getStatusDetails(null));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenOrderIdDoesntExists() {
        Assertions.assertThrows(OrderNotFoundException.class,
                () -> failedQuery.getStatusDetails(new OrderId("unknown_id")));
    }

    @Test
    void shouldReturnCurrentOrderStatusDetails() {
        Optional<OrderStatusDetails> details = query.getStatusDetails(new OrderId("id"));
        Assertions.assertNotNull(details);
        Assertions.assertTrue(details.isPresent());
        Assertions.assertNotNull(details.get().status());
    }
}
