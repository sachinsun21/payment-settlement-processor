package com.lbg.payment.processor.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbg.payment.processor.dto.PaymentEvent;
import com.lbg.payment.processor.repository.PaymentOutcomeRepository;
import com.lbg.payment.processor.service.PaymentProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentProcessorConsumerService {

  private final PaymentOutcomeRepository paymentRepository;
  private final PaymentProcessorService paymentService;
  private final ObjectMapper objectMapper;


  @KafkaListener(id = "payments-consumer", topics = "payments.submitted", groupId = "payment-processor-service")
  public void listen(String value,
                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                     @Header(KafkaHeaders.RECEIVED_KEY) String key) throws JsonProcessingException {
    long start = System.currentTimeMillis();
    PaymentEvent event = objectMapper.readValue(value, PaymentEvent.class);
    log.info("Payment Landed: [ID: {}]", event.paymentId());

    try {
      // 1. Idempotency Check
      if (paymentRepository.existsByPaymentId(event.paymentId())) {
        log.warn("Duplicate detected: {}", event.paymentId());
        //ack.acknowledge();
        return;
      }

      // 2. Process and Save
      paymentService.processAndSave(event, start);

      // 3. Manual Ack
      //ack.acknowledge();
      log.info("Payment Fortified: {}", event.paymentId());

    } catch (Exception e) {
      log.error("Critical Error — Kafka will retry: {}", e.getMessage());
      // No ack → Kafka redelivers
    }
  }
}