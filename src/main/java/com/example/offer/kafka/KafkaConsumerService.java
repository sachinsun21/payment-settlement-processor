package com.example.offer.kafka;

import com.example.offer.aws.S3Service;
import com.example.offer.dto.OfferDTO;
import com.example.offer.dto.PaymentTransaction;
import com.example.offer.entity.PaymentEntity;
import com.example.offer.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

  private final PaymentRepository paymentRepository;
  private final S3Service s3Service; // <--- Injecting Phase 2 Logic

  @KafkaListener(topics = "payment-transactions-v1", groupId = "payment-settlement-service")
  public void consume(@Payload PaymentTransaction txn, Acknowledgment ack) {
    log.info("🐦 Pigeon Landed: {} [ID: {}]", txn.idempotencyKey(), txn.txnId());

    try {
      // 1. Idempotency Check (Level 1)
      if (paymentRepository.existsByIdempotencyKey(txn.idempotencyKey())) {
        log.warn("⚠️ Duplicate detected: {}", txn.idempotencyKey());
        ack.acknowledge();
        return;
      }

      // 2. PHASE 2: Evidence Upload (Real S3)
      // We simulate a receipt/json record for the audit trail
      String evidenceData = "Audit Record for TXN: " + txn.txnId();
      String s3Key = s3Service.uploadFile("evidence-" + txn.txnId() + ".txt", evidenceData.getBytes());

      // 3. Save to Vault (Now including the S3 Reference)
      saveToVault(txn, s3Key);

      ack.acknowledge();
      log.info("🏰 Payment Fortified with Evidence: {}", txn.txnId());

    } catch (DataIntegrityViolationException e) {
      log.warn("⚠️ Race Condition blocked by DB: {}", txn.idempotencyKey());
      ack.acknowledge();
    } catch (RuntimeException s3Error) {
      // If S3 fails, this catch block ensures we DO NOT ACK
      log.error(" S3 Failure for txn {}: {}. Retrying...", txn.txnId(), s3Error.getMessage());
    } catch (Exception e) {
      log.error("💥 Critical System Error: {}", e.getMessage());
    }
  }

  private void saveToVault(PaymentTransaction txn, String s3Key) {
    var entity = PaymentEntity.builder()
            .txnId(txn.txnId())
            .idempotencyKey(txn.idempotencyKey())
            .sourceAccount(txn.sourceAccount())
            .destinationAccount(txn.destinationAccount())
            .amount(txn.amount())
            .currency(txn.currency())
            .s3EvidenceKey(s3Key) // <--- Link the S3 Key in Postgres
            .processedAt(Instant.now())
            .build();
    paymentRepository.save(entity);
  }
}
