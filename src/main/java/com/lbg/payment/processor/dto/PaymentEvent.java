package com.lbg.payment.processor.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record PaymentEvent(
        UUID paymentId,
        String debitAccountId,
        String creditAccountId,
        BigDecimal amount,
        String currency,
        String reference,
        Instant timestamp
) {}
