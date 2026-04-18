package com.lbg.payment.processor.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lbg.payment.processor.entity.Account;
import com.lbg.payment.processor.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@Slf4j
public class AccountDataLoader {

    @Autowired
    private  AccountRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Account> loadJsonAndGetAccounts() throws IOException {
        ClassPathResource resource = new ClassPathResource("accounts.json");
        Account[] accounts = objectMapper.readValue(resource.getFile(), Account[].class);
        return Arrays.asList(accounts);
    }

    @PostConstruct
    public void init() throws IOException {
        List<Account> accounts = loadJsonAndGetAccounts();
        repository.saveAll(accounts);
    }
}


