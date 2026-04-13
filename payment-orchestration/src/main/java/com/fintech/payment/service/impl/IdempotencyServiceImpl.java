package com.fintech.payment.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fintech.payment.dto.PaymentResponse;
import com.fintech.payment.entity.IdempotencyRecord;
import com.fintech.payment.exception.PaymentException;
import com.fintech.payment.repository.IdempotencyRepository;
import com.fintech.payment.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyRepository repository;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<PaymentResponse> get(String key) {

        validateKey(key);

        try {
            return repository.findById(key)
                    .map(record -> deserialize(record.getResponse()));
        } catch (Exception e) {
            throw new PaymentException(
                    "Error fetching idempotency record",
                    "IDEMPOTENCY_FETCH_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @Override
    public void save(String key, PaymentResponse response) {

        validateKey(key);

        try {
            IdempotencyRecord record = new IdempotencyRecord();
            record.setIdempotencyKey(key);
            record.setResponse(objectMapper.writeValueAsString(response));

            repository.save(record);

        } catch (Exception e) {
            throw new PaymentException(
                    "Error saving idempotency record",
                    "IDEMPOTENCY_SAVE_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    // 🔹 Helper Methods

    private PaymentResponse deserialize(String json) {
        try {
            return objectMapper.readValue(json, PaymentResponse.class);
        } catch (Exception e) {
            throw new PaymentException(
                    "Error parsing idempotency response",
                    "IDEMPOTENCY_PARSE_ERROR",
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private void validateKey(String key) {
        if (key == null || key.isBlank()) {
            throw new PaymentException(
                    "Idempotency key is required",
                    "INVALID_IDEMPOTENCY_KEY",
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}