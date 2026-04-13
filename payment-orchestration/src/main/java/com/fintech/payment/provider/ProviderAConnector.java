package com.fintech.payment.provider;

import com.fintech.payment.dto.PaymentRequest;
import com.fintech.payment.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component("A")
public class ProviderAConnector implements PaymentProvider {

    @Override
    public PaymentResponse process(PaymentRequest request) {
        return new PaymentResponse(null, "SUCCESS", "A");
    }
}
