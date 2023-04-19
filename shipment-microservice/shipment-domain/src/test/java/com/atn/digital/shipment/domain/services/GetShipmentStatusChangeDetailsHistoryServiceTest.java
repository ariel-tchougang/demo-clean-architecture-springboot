package com.atn.digital.shipment.domain.services;

import com.atn.digital.shipment.domain.exceptions.ShipmentNotFoundException;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentStatusChangeDetails;
import com.atn.digital.shipment.domain.ports.in.queries.GetShipmentStatusChangeDetailsHistoryQuery;
import com.atn.digital.shipment.domain.ports.out.persistence.FailedLoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.FakeLoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.LoadShipmentPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class GetShipmentStatusChangeDetailsHistoryServiceTest {

    private final LoadShipmentPort port = new FakeLoadShipmentPort();
    private final GetShipmentStatusChangeDetailsHistoryQuery query = new GetShipmentStatusChangeDetailsHistoryService(port);
    private final LoadShipmentPort failedLoader = new FailedLoadShipmentPort();
    private final GetShipmentStatusChangeDetailsHistoryQuery failedQuery =
            new GetShipmentStatusChangeDetailsHistoryService(failedLoader);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> query.getStatusChangeDetailsHistory(null));
    }

    @Test
    void shouldThrowShipmentNotFoundExceptionWhenShipmentIdDoesntExists() {
        Assertions.assertThrows(ShipmentNotFoundException.class,
                () -> failedQuery.getStatusChangeDetailsHistory(new ShipmentId("unknown_id")));
    }

    @Test
    void shouldReturnStatusChangeDetailsHistory() {
        List<ShipmentStatusChangeDetails> history = query.getStatusChangeDetailsHistory(new ShipmentId("id"));
        Assertions.assertNotNull(history);
        Assertions.assertFalse(history.isEmpty());
    }
}
