package com.fintech.payment.service;

import com.fintech.payment.dto.PaymentResponse;

import java.util.Optional;

public interface IdempotencyService {

    Optional<PaymentResponse> get(String key);

    void save(String key, PaymentResponse response);
}
