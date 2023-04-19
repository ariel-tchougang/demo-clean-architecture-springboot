package com.atn.digital.inventory.application.adapters.out.notifications;

import com.atn.digital.inventory.domain.ports.out.notifications.InsufficientStockPublisher;
import com.atn.digital.inventory.domain.ports.out.notifications.SufficientStockPublisher;

public abstract class StockEventPublisherAdapter implements InsufficientStockPublisher, SufficientStockPublisher { }
