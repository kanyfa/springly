package com.bank.demo.service;

import com.bank.demo.entity.Account;
import com.bank.demo.entity.Client;
import com.bank.demo.repository.AccountRepository;
import com.bank.demo.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ClientRepository clientRepository;

    public Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public BigDecimal getBalance(String accountNumber) {
        Account account = getAccountByNumber(accountNumber);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        return account.getBalance();
    }

    public Account createAccount(String accountNumber, BigDecimal initialBalance, Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RuntimeException("Client not found"));
        Account account = new Account(accountNumber, initialBalance, client);
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        Account fromAccount = getAccountByNumber(fromAccountNumber);
        Account toAccount = getAccountByNumber(toAccountNumber);

        if (fromAccount == null || toAccount == null) {
            throw new RuntimeException("Account not found");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}