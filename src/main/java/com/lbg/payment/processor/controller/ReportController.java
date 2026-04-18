package com.lbg.payment.processor.controller;

import com.lbg.payment.processor.dto.ReportSummary;
import com.lbg.payment.processor.entity.PaymentOutcome;
import com.lbg.payment.processor.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/summary")
    public ReportSummary getReportSummary(@RequestParam(name = "fromDate") LocalDate fromDate,
                                        @RequestParam(name = "toDate") LocalDate toDate) {
        return reportService.getSummary(fromDate, toDate);
    }
    @GetMapping("/activity")
    public Page<PaymentOutcome> getReportActivity(@RequestParam(name = "status", required = false) String status,
                                                  @RequestParam(name = "accountId",required = false) String accountId
                                           ) {
        return reportService.getReportActivity(status, accountId);
    }
}
