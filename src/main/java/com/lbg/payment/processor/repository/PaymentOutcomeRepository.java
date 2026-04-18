package com.lbg.payment.processor.repository;

import com.lbg.payment.processor.entity.PaymentOutcome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentOutcomeRepository extends JpaRepository<PaymentOutcome, Long> {
    boolean existsByPaymentId(UUID paymentId);
    List<PaymentOutcome> findByStatus(String status);
    List<PaymentOutcome> findByDebitAccountIdOrCreditAccountId(
            String debitAccountId, String creditAccountId
    );
}