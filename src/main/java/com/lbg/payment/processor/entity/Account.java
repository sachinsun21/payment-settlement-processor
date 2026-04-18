package com.lbg.payment.processor.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "accounts")
@Data
public class Account {
    @Id
    private String accountId;

    private String accountName;

    private String accountType;

    private String status;

    private String currency;

    private LocalDate openedDate;
}
