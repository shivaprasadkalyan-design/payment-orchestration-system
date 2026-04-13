package com.fintech.payment.exception;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends PaymentException {
    public InvalidRequestException(String message) {
        super(message, "INVALID_REQUEST", HttpStatus.BAD_REQUEST);
    }
}
