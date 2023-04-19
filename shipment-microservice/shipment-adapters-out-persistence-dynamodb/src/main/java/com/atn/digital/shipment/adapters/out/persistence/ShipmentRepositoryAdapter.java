package com.atn.digital.shipment.adapters.out.persistence;

import com.atn.digital.shipment.domain.ports.out.persistence.CreateNewShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.LoadShipmentPort;
import com.atn.digital.shipment.domain.ports.out.persistence.UpdateShipmentPort;

public abstract class ShipmentRepositoryAdapter implements CreateNewShipmentPort, LoadShipmentPort, UpdateShipmentPort { }
