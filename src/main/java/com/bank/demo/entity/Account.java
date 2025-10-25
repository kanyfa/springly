package com.bank.demo.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    // Constructors, getters, setters
    public Account() {}

    public Account(String accountNumber, BigDecimal balance, Client client) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.client = client;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
}