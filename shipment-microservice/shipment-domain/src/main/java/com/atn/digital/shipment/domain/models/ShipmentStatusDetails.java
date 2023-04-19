package com.atn.digital.shipment.domain.models;

import java.time.Instant;

public record ShipmentStatusDetails(ShipmentStatus status, Instant changeDate, String reason) { }
