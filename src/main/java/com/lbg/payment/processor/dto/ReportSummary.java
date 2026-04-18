package com.lbg.payment.processor.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ReportSummary {
    private Map<String, Long> paymentsPerStatus;
    private Long totalAmount;
}
