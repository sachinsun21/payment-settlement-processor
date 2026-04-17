package com.example.offer.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentTransaction(
        UUID txnId,
        String idempotencyKey, // The "Fortress" Shield
        String sourceAccount,
        String destinationAccount,
        BigDecimal amount,
        String currency,
        Instant createdAt
) {
    // Lead Flex: Compact Constructor for Validation (Java 21)
    public PaymentTransaction {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment must be positive, Sachin!");
        }
    }
}

