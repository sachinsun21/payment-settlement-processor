package com.example.offer.kafka;

import com.example.offer.aws.S3Service;
import com.example.offer.dto.OfferDTO;
import com.example.offer.dto.PaymentTransaction;
import com.example.offer.entity.PaymentEntity;
import com.example.offer.repository.PaymentRepository;
import com.example.offer.service.PaymentService;
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
  private final PaymentService paymentService; // Inject the new service

  @KafkaListener(topics = "payment-transactions-v1", groupId = "payment-settlement-service")
  public void consume(@Payload PaymentTransaction txn, Acknowledgment ack) {
    log.info("🕊️ Pigeon Landed: {} [ID: {}]", txn.idempotencyKey(), txn.txnId());

    try {
      // 1. Idempotency Check
      if (paymentRepository.existsByIdempotencyKey(txn.idempotencyKey())) {
        log.warn("Duplicate detected: {}", txn.idempotencyKey());
        ack.acknowledge();
        return;
      }

      // 2. S3 Evidence Upload
      String evidenceData = "Audit Record for TXN: " + txn.txnId();
      String s3Key = s3Service.uploadFile("evidence-" + txn.txnId() + ".txt", evidenceData.getBytes());

      // 3. HAND-OFF TO SERVICE
      // This call is now @Transactional
      paymentService.saveToVault(txn, s3Key);

      // 4. MANUAL ACK
      ack.acknowledge();
      log.info("Payment Fortified with Evidence: {}", txn.txnId());

    } catch (Exception e) {
      // If saveToVault fails, we reach here.
      // We DO NOT call ack.acknowledge(), so Kafka will retry.
      log.error("Critical System Error: {}", e.getMessage());
    }
  }
}
