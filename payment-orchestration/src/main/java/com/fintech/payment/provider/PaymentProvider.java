package com.fintech.payment.provider;

import com.fintech.payment.dto.PaymentRequest;
import com.fintech.payment.dto.PaymentResponse;

public interface PaymentProvider {
    PaymentResponse process(PaymentRequest request);
}
