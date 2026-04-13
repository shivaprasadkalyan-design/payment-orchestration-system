package com.fintech.payment.service;

import com.fintech.payment.dto.PaymentRequest;
import com.fintech.payment.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request, String key);

    PaymentResponse getPayment(String id);
}
