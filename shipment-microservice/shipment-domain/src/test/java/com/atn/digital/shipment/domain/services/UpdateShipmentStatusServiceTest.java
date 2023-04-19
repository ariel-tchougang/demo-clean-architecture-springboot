package com.atn.digital.shipment.domain.services;

import com.atn.digital.shipment.domain.exceptions.ShipmentNotFoundException;
import com.atn.digital.shipment.domain.models.ShipmentStatus;
import com.atn.digital.shipment.domain.models.ShipmentStatusDetails;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.UpdateShipmentStatusUseCase;
import com.atn.digital.shipment.domain.ports.out.notifications.InMemoryShipmentEventPublisher;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentStatusChangedEvent;
import com.atn.digital.shipment.domain.ports.out.persistence.FailedLoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.FakeLoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.FakeUpdateShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.LoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.UpdateShipmentPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateShipmentStatusServiceTest {

    private final LoadShipmentPort loadShipmentPort = new FakeLoadShipmentPort();
    private final UpdateShipmentPort updateShipmentPort = new FakeUpdateShipmentPort();
    private final InMemoryShipmentEventPublisher publisher = new InMemoryShipmentEventPublisher();
    private final UpdateShipmentStatusUseCase service =
            new UpdateShipmentStatusService(loadShipmentPort, updateShipmentPort, publisher);
    private final LoadShipmentPort failedLoader = new FailedLoadShipmentPort();
    private final UpdateShipmentStatusUseCase failedService =
            new UpdateShipmentStatusService(failedLoader, updateShipmentPort, publisher);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.handle(null));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenTargetOrderIsNotFound() {
        UpdateShipmentStatusCommand command =
                new UpdateShipmentStatusCommand("shipmentId", ShipmentStatus.ON_THE_WAY, "payment");
        Assertions.assertThrows(ShipmentNotFoundException.class, () -> failedService.handle(command));
    }

    @Test
    void shouldUpdateStatus() {
        UpdateShipmentStatusCommand command =
                new UpdateShipmentStatusCommand("shipmentId", ShipmentStatus.ON_THE_WAY, "sent");

        ShipmentStatusDetails details = service.handle(command);
        Assertions.assertEquals(ShipmentStatus.ON_THE_WAY, details.status());
    }

    @Test
    void shouldPublishShipmentStatusChangedEvent() {
        UpdateShipmentStatusCommand command =
                new UpdateShipmentStatusCommand("shipmentId", ShipmentStatus.ON_THE_WAY, "sent");

        ShipmentStatusDetails details = service.handle(command);
        Assertions.assertEquals(ShipmentStatus.ON_THE_WAY, details.status());
        ShipmentStatusChangedEvent event = new ShipmentStatusChangedEvent(
                "shipmentId",
                "orderId",
                ShipmentStatus.CREATED.name(),
                ShipmentStatus.ON_THE_WAY.name());
        assertThat(publisher.statusChangedShipmentEvents()).containsExactly(event);
    }
}
