package com.example.offer.service;

import com.example.offer.dto.PaymentTransaction;
import com.example.offer.entity.PaymentEntity;
import com.example.offer.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional // <--- Essential for Atomic DB Saves
    public void saveToVault(PaymentTransaction txn, String s3Key) {
        var entity = PaymentEntity.builder()
                .txnId(txn.txnId())
                .idempotencyKey(txn.idempotencyKey())
                .sourceAccount(txn.sourceAccount())
                .destinationAccount(txn.destinationAccount())
                .amount(txn.amount())
                .currency(txn.currency())
                .s3EvidenceKey(s3Key)
                .processedAt(Instant.now())
                .build();

        paymentRepository.save(entity);
    }
}
