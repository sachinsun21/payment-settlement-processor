package com.lbg.payment.processor.service;

import com.lbg.payment.processor.entity.Account;
import com.lbg.payment.processor.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository repository;

    public void validateTransaction(String debitId, String creditId) {
        Account debit = repository.findById(debitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Debit Account Not Found"));

        Account credit = repository.findById(creditId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Credit Account Not Found"));

        if ("SUSPENDED".equalsIgnoreCase(debit.getStatus())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Debit Account Suspended");
        }

        if ("SUSPENDED".equalsIgnoreCase(credit.getStatus())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Credit Account Suspended");
        }

        // If it reaches here, accounts are ACTIVE
    }
}
