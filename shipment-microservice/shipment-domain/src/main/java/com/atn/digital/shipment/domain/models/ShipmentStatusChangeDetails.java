package com.atn.digital.shipment.domain.models;

import java.time.Instant;

public record ShipmentStatusChangeDetails(ShipmentStatus oldStatus, ShipmentStatus newStatus, Instant changeDate,
                                          String reason) { }
