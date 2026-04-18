package com.lbg.payment.processor.service;

import com.lbg.payment.processor.dto.MetricsSummary;
import com.lbg.payment.processor.dto.PaymentEvent;
import com.lbg.payment.processor.entity.PaymentOutcome;
import com.lbg.payment.processor.repository.PaymentOutcomeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessorService {

    private final PaymentOutcomeRepository repository;

    private final AtomicLong totalProcessed = new AtomicLong();
    private final AtomicLong totalHeld = new AtomicLong();
    private final AtomicLong totalRejected = new AtomicLong();
    private final AtomicLong totalProcessingTime = new AtomicLong();

    private static final BigDecimal HELD_THRESHOLD = new BigDecimal("250000");

    @Transactional
    public void processAndSave(PaymentEvent event, long startTime) {

        String status = event.amount().compareTo(HELD_THRESHOLD) > 0
                ? "HELD" : "PROCESSED";

        long processingTime = System.currentTimeMillis() - startTime;

        PaymentOutcome outcome = PaymentOutcome.builder()
                .paymentId(event.paymentId())
                .debitAccountId(event.debitAccountId())
                .creditAccountId(event.creditAccountId())
                .amount(event.amount())
                .currency(event.currency())
                .status(status)
                .processedAt(Instant.now())
                .processingTimeMs(processingTime)
                .build();

        repository.save(outcome);

        // Update live counters
        if ("HELD".equals(status)) totalHeld.incrementAndGet();
        else totalProcessed.incrementAndGet();
        totalProcessingTime.addAndGet(processingTime);

        log.info("Payment {} → {}", event.paymentId(), status);
    }

    public MetricsSummary getMetrics() {
        long processed = totalProcessed.get();
        long held = totalHeld.get();
        long total = processed + held;
        double avg = total > 0 ? (double) totalProcessingTime.get() / total : 0;
        return new MetricsSummary(processed, held, totalRejected.get(), avg);
    }
}