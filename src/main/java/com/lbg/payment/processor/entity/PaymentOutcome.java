package com.lbg.payment.processor.entity;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "payment_outcomes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_payment_id", columnNames = {"paymentId"})
})
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class PaymentOutcome {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private UUID paymentId; // The UUID from the Ingestor

    @Column(nullable = false)
    private String debitAccountId;

    @Column(nullable = false)
    private String creditAccountId;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private String status; // PROCESSED, HELD, REJECTED

    @Column(nullable = false)
    private Instant processedAt;

    @Column(nullable = false)
    private Long processingTimeMs;

    @Column(name = "idempotency_key", unique = true)
    private String idempotencyKey; // From original event for safety

    @Version
    private Integer version; // Optimistic Locking for concurrent Virtual Threads
}
