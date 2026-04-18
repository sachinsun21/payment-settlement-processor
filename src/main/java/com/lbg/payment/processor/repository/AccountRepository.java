package com.lbg.payment.processor.repository;

import com.lbg.payment.processor.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the Account Register (H2).
 * Used by AccountService to validate account existence and status.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    // Inherits findById(String id), existsById(String id), etc.
}
