package com.example.offer.kafka;

import com.example.offer.dto.OfferDTO;
import com.example.offer.dto.PaymentTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    //private static final String TOPIC="offer-events";
    // 1. Point to the NEW topic
    private static final String TOPIC = "payment-transactions-v1";

    // 2. Update to use the Record
    private final KafkaTemplate<String, PaymentTransaction> kafkaTemplate;

    public void sendPayment(PaymentTransaction txn) {
        log.info("📤 Launching Payment Pigeon: {}", txn.idempotencyKey());

        // 3. THE LEAD MOVE: Use idempotencyKey as the Kafka Message Key
        // This ensures all retries for this payment stay in the same partition.
        kafkaTemplate.send(TOPIC, txn.idempotencyKey(), txn)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("✅ Pigeon Delivered to Kafka: offset {}",
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("❌ Wing Clip! Kafka delivery failed: {}", ex.getMessage());
                    }
                });
    }

    /*private final KafkaTemplate<String, OfferDTO> kafkaTemplate;

    public void publishOfferCreated(OfferDTO dto){

        kafkaTemplate.send(TOPIC,"CREATED",dto);
    }
   public void publishOfferUpdated(OfferDTO dto){

        kafkaTemplate.send(TOPIC,"UPDATED",dto);
   }*/
}
