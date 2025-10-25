package com.bank.demo.controller;

import com.bank.demo.entity.Account;
import com.bank.demo.entity.Client;
import com.bank.demo.service.AccountService;
import com.bank.demo.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class BankController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/clients")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<Client>> getClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/solde")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<BigDecimal> getBalance(@RequestParam String accountNumber) {
        try {
            BigDecimal balance = accountService.getBalance(accountNumber);
            return ResponseEntity.ok(balance);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, Object> request) {
        try {
            String accountNumber = (String) request.get("accountNumber");
            BigDecimal initialBalance = new BigDecimal(request.get("initialBalance").toString());
            Long clientId = Long.valueOf(request.get("clientId").toString());

            Account account = accountService.createAccount(accountNumber, initialBalance, clientId);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> transfer(@RequestBody Map<String, Object> request) {
        try {
            String fromAccountNumber = (String) request.get("fromAccountNumber");
            String toAccountNumber = (String) request.get("toAccountNumber");
            BigDecimal amount = new BigDecimal(request.get("amount").toString());

            accountService.transfer(fromAccountNumber, toAccountNumber, amount);
            return ResponseEntity.ok("Transfer successful");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}