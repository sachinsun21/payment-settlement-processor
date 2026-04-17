package com.example.offer.controller;

import com.example.offer.dto.PaymentTransaction;
import com.example.offer.kafka.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final KafkaProducerService kafkaProducerService;

    @PostMapping
    public ResponseEntity<String> initiatePayment(@RequestBody PaymentTransaction txn) {
        log.info("🚀 API Triggered: Launching Payment Pigeon for TxnId: {}", txn.txnId());

        // The Hand-off
        kafkaProducerService.sendPayment(txn);

        return ResponseEntity.accepted()
                .body("✅ Pigeon is airborne! TxnId: " + txn.txnId());
    }
}
