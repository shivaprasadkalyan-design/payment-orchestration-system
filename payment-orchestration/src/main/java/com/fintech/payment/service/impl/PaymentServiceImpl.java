package com.fintech.payment.service.impl;

import com.fintech.payment.dto.PaymentRequest;
import com.fintech.payment.dto.PaymentResponse;
import com.fintech.payment.entity.Payment;
import com.fintech.payment.enums.PaymentStatus;
import com.fintech.payment.enums.Provider;
import com.fintech.payment.exception.InvalidRequestException;
import com.fintech.payment.exception.PaymentException;
import com.fintech.payment.exception.ProviderException;
import com.fintech.payment.exception.ResourceNotFoundException;
import com.fintech.payment.provider.PaymentProvider;
import com.fintech.payment.repository.PaymentRepository;
import com.fintech.payment.routing.RoutingEngine;
import com.fintech.payment.service.IdempotencyService;
import com.fintech.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final RoutingEngine routingEngine;
    private final IdempotencyService idempotencyService;
    private final PaymentRepository paymentRepository;
    private final Map<String, PaymentProvider> providerMap;

    @Override
    public PaymentResponse createPayment(PaymentRequest request, String key) {

        validateRequest(request, key);

        // Idempotency
        Optional<PaymentResponse> existing = idempotencyService.get(key);
        if (existing.isPresent()) return existing.get();

        Provider provider = routingEngine.route(request.getPaymentMethod());

        PaymentResponse response = processWithRetry(provider, request);

        Payment payment = savePayment(request, provider, response);

        response.setPaymentId(payment.getId());

        idempotencyService.save(key, response);

        return response;
    }

    @Override
    public PaymentResponse getPayment(String id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        return new PaymentResponse(
                payment.getId(),
                payment.getStatus().name(),
                payment.getProvider().name()
        );
    }

    //  Private Helper Methods
    private void validateRequest(PaymentRequest request, String key) {

        if (request == null) {
            throw new InvalidRequestException("Request cannot be null");
        }

        if (key == null || key.isBlank()) {
            throw new InvalidRequestException("Idempotency key is required");
        }

        if (request.getPaymentMethod() == null) {
            throw new InvalidRequestException("Payment method is required");
        }
    }

    private PaymentResponse processWithRetry(Provider provider, PaymentRequest request) {

        int retry = 0;

        while (retry < 3) {
            try {
                PaymentResponse response = providerMap.get(provider.name()).process(request);

                if ("SUCCESS".equals(response.getStatus())) {
                    return response;
                }
            } catch (Exception e) {
                // log error
            }
            retry++;
        }

        // Failover
        Provider fallback = provider == Provider.A ? Provider.B : Provider.A;

        PaymentResponse fallbackResponse = providerMap.get(fallback.name()).process(request);

        if (!"SUCCESS".equals(fallbackResponse.getStatus())) {
            throw new ProviderException("Both providers failed");
        }

        return fallbackResponse;
    }

    private Payment savePayment(PaymentRequest request, Provider provider, PaymentResponse response) {

        try {
            Payment payment = new Payment();
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setProvider(provider);
            payment.setStatus(PaymentStatus.valueOf(response.getStatus()));

            return paymentRepository.save(payment);

        } catch (Exception e) {
            throw new PaymentException(
                    "Database error while saving payment",
                    "DB_ERROR",
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}