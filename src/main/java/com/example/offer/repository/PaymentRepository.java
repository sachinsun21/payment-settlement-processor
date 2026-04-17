package com.example.offer.repository;

import com.example.offer.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, UUID> {
    // Check if we've seen this idempotency key before
    boolean existsByIdempotencyKey(String idempotencyKey);
}

