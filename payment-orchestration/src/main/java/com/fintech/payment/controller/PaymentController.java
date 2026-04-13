package com.fintech.payment.controller;

import com.fintech.payment.dto.PaymentRequest;
import com.fintech.payment.dto.PaymentResponse;
import com.fintech.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment API", description = "Payment Orchestration APIs")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Create Payment", description = "Creates and processes a payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal error")
    })
    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @RequestBody PaymentRequest request,
            @RequestHeader("Idempotency-Key") String key) {

        return ResponseEntity.ok(paymentService.createPayment(request, key));
    }

    @Operation(summary = "Get Payment", description = "Fetch payment by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(paymentService.getPayment(id));
    }
}