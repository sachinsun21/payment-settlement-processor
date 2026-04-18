package com.lbg.payment.processor.controller;

import com.lbg.payment.processor.dto.MetricsSummary;
import com.lbg.payment.processor.service.PaymentProcessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {
    private final PaymentProcessorService processorService;

    @GetMapping("/summary")
    public ResponseEntity<MetricsSummary> getSummary() {
        return ResponseEntity.ok(processorService.getMetrics());
    }
}
