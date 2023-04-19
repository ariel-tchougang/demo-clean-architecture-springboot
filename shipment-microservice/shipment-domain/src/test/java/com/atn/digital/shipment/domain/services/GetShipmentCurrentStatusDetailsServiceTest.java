package com.atn.digital.shipment.domain.services;

import com.atn.digital.shipment.domain.exceptions.ShipmentNotFoundException;
import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;
import com.atn.digital.shipment.domain.ports.in.queries.GetShipmentCurrentStatusDetailsQuery;
import com.atn.digital.shipment.domain.ports.out.persistence.FailedLoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.FakeLoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.LoadShipmentPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class GetShipmentCurrentStatusDetailsServiceTest {

    private final LoadShipmentPort port = new FakeLoadShipmentPort();
    private final GetShipmentCurrentStatusDetailsQuery query = new GetShipmentCurrentStatusDetailsService(port);
    private final LoadShipmentPort failedLoader = new FailedLoadShipmentPort();
    private final GetShipmentCurrentStatusDetailsQuery failedQuery = new GetShipmentCurrentStatusDetailsService(failedLoader);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> query.getStatusDetails(null));
    }

    @Test
    void shouldThrowShipmentNotFoundExceptionWhenShipmentIdDoesntExists() {
        Assertions.assertThrows(ShipmentNotFoundException.class,
                () -> failedQuery.getStatusDetails(new ShipmentId("unknown_id")));
    }

    @Test
    void shouldReturnCurrentShipmentStatusDetails() {
        Optional<ShipmentStatusDetails> details = query.getStatusDetails(new ShipmentId("id"));
        Assertions.assertNotNull(details);
        Assertions.assertTrue(details.isPresent());
        Assertions.assertNotNull(details.get().status());
    }
}
