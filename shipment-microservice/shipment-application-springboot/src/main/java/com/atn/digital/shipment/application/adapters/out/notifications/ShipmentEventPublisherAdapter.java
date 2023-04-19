package com.atn.digital.shipment.application.adapters.out.notifications;

import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentCreatedEventPublisherPort;
import com.atn.digital.shipment.domain.ports.out.notifications.ShipmentStatusChangedEventPublisherPort;

public abstract class ShipmentEventPublisherAdapter
        implements ShipmentCreatedEventPublisherPort, ShipmentStatusChangedEventPublisherPort { }
