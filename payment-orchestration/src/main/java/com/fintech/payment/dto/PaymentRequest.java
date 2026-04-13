package com.fintech.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Payment Request DTO")
public class PaymentRequest {

    @Schema(example = "1000", description = "Payment amount")
    private Double amount;

    @Schema(example = "INR")
    private String currency;

    @Schema(example = "CARD / UPI")
    private String paymentMethod;
}
