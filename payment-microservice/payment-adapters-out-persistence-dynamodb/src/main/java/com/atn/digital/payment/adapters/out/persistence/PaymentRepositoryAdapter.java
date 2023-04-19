package com.atn.digital.payment.adapters.out.persistence;

import com.atn.digital.payment.domain.ports.out.persistence.LoadPaymentDetailsPort;
import com.atn.digital.payment.domain.ports.out.persistence.SavePaymentDetailsPort;

public abstract class PaymentRepositoryAdapter implements
        SavePaymentDetailsPort, LoadPaymentDetailsPort { }
