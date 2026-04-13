package com.fintech.payment.provider;

import com.fintech.payment.dto.PaymentRequest;
import com.fintech.payment.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component("B")
public class ProviderBConnector implements PaymentProvider {

    @Override
    public PaymentResponse process(PaymentRequest request) {
        return new PaymentResponse(null, "FAILED", "B");
    }
}
