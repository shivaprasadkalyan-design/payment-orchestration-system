package com.fintech.payment.exception;

import org.springframework.http.HttpStatus;

public class ProviderException extends PaymentException {
    public ProviderException(String message) {
        super(message, "PROVIDER_ERROR", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
