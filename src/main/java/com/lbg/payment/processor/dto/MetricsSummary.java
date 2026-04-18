package com.lbg.payment.processor.dto;

public record MetricsSummary(
        long totalProcessed,
        long totalHeld,
        long totalRejected,
        double avgProcessingTimeMs
) {}
