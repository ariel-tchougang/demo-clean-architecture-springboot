package com.atn.digital.payment.adapters.out.persistence.dynamodb;

import com.atn.digital.payment.domain.models.PaymentDetails;
import com.atn.digital.payment.domain.models.PaymentStatus;

import java.math.BigDecimal;
import java.time.Instant;

public class PaymentDetailsMapper {

    public PaymentDetails toPaymentDetails(PaymentDetailsItem item) {
        return new PaymentDetails(
                item.getId(),
                item.getCustomerId(),
                item.getOrderId(),
                BigDecimal.valueOf(item.getAmount()),
                item.getReason(),
                Instant.ofEpochMilli(item.getInstant()),
                PaymentStatus.getStatus(item.getStatus())
        );
    }

    public PaymentDetailsItem toPaymentDetailsItem(PaymentDetails details) {
        PaymentDetailsItem item = new PaymentDetailsItem();
        item.setCustomerId(details.customerId());
        item.setOrderId(details.orderId());
        item.setAmount(details.amount().doubleValue());
        item.setReason(details.reason());
        item.setStatus(details.status().name());
        return item;
    }
}
