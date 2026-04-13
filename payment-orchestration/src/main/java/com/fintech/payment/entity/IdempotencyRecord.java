package com.fintech.payment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "idempotency_keys")
@Getter
@Setter
public class IdempotencyRecord {

    @Id
    @Column(name = "idempotency_key")   // ✅ renamed
    private String idempotencyKey;

    @Column(columnDefinition = "TEXT")
    private String response;

}
