package com.atn.digital.shipment.domain.services;

import com.atn.digital.shipment.domain.models.Shipment.ShipmentId;
import com.atn.digital.shipment.domain.models.ShipmentItem;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentCommand;
import com.atn.digital.shipment.domain.ports.in.usecases.CreateNewShipmentUseCase;
import com.atn.digital.shipment.domain.ports.out.notifications.InMemoryShipmentEventPublisher;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentCreatedEvent;
import com.atn.digital.shipment.domain.ports.out.persistence.CreateNewShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.DummyCreateNewShipmentPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CreateNewShipmentServiceTest {

    private final CreateNewShipmentPort port = new DummyCreateNewShipmentPort();
    private final InMemoryShipmentEventPublisher publisher = new InMemoryShipmentEventPublisher();

    private final CreateNewShipmentUseCase service = new CreateNewShipmentService(port, publisher);

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCommandIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.handle(null));
    }

    @Test
    void shouldReturnNewOrderId() {
        List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
        CreateNewShipmentCommand command = new CreateNewShipmentCommand("orderId", "address", items);
        ShipmentId bean = service.handle(command);
        Assertions.assertNotNull(bean);
        Assertions.assertNotNull(bean.getId());
    }

    @Test
    void shouldPublishShipmentCreatedEvent() {
        List<ShipmentItem> items = Arrays.asList(new ShipmentItem("1", "name", 1));
        CreateNewShipmentCommand command = new CreateNewShipmentCommand("orderId", "address", items);
        ShipmentId bean = service.handle(command);
        ShipmentCreatedEvent event = new ShipmentCreatedEvent(
                bean.getId(),
                "orderId");
        assertThat(publisher.createdShipmentEvents()).containsExactly(event);
    }
}
