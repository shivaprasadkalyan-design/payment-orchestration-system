package com.fintech.payment.entity;

import com.fintech.payment.enums.PaymentStatus;
import com.fintech.payment.enums.Provider;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private Double amount;
    private String currency;
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private int retryCount;
}
