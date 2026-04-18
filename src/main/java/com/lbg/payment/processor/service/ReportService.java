package com.lbg.payment.processor.service;

import com.lbg.payment.processor.dto.ReportSummary;
import com.lbg.payment.processor.entity.PaymentOutcome;
import com.lbg.payment.processor.repository.PaymentOutcomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private PaymentOutcomeRepository outcomeRepository;

    public ReportSummary getSummary(LocalDate fromDate, LocalDate toDate) {
        Instant fromInstant = fromDate.atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
        Instant toInstant = toDate.atStartOfDay().toInstant(java.time.ZoneOffset.UTC);
        List<PaymentOutcome> outcomes = outcomeRepository.findByProcessedAtBetween(fromInstant, toInstant);
        ReportSummary summary = new ReportSummary();
        summary.setPaymentsPerStatus(outcomes.stream()
            .collect(Collectors.groupingBy(PaymentOutcome::getStatus, Collectors.counting())));
        summary.setTotalAmount(outcomes.stream()
            .mapToLong(outcome -> outcome.getAmount().longValue())
            .sum());
        return summary;
    }

    public Page<PaymentOutcome> getReportActivity(String status, String accountId){

        PageRequest pageableRequest = PageRequest.of(0, 10);

        List<PaymentOutcome> filteredResults = outcomeRepository.findAll(pageableRequest).stream()
            .filter(outcome -> outcome.getStatus().equals(status) &&
                (outcome.getCreditAccountId().equals(accountId) || outcome.getDebitAccountId().equals(accountId)))
            .toList();

        return new PageImpl<>(filteredResults, pageableRequest, filteredResults.size());
    }

}
