package com.fintech.payment.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends PaymentException {
    public ResourceNotFoundException(String message) {
        super(message, "PAYMENT_NOT_FOUND", HttpStatus.NOT_FOUND);
    }
}
