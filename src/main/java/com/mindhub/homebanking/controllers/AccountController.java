package com.mindhub.homebanking.controllers;


import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAllAccountsDTO();
    }

    @GetMapping("/api/account/{id}")
    public AccountDTO getAccount(@PathVariable Long id) {
        return accountService.FindAccountIDDTO(id);
    }


    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<?> createAccount(
            @RequestParam String password,
            @RequestParam String typeAccount,
            Authentication authentication) {

        Client clientCurrent = clientService.clientFindByEmail(authentication.getName());
        Set<Account> accountsCurrent = clientCurrent.getAccounts().stream()
                .filter(account -> account.getAccount_EnabledOrDisabled() == EnabledOrDisabled.ENABLED).collect(Collectors.toSet());

        if (clientCurrent == null) {
            return new ResponseEntity<>("You aren't an authenticated customer", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("The password is empty", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(password, clientCurrent.getPassword())) {
            return new ResponseEntity<>("The password is incorrect", HttpStatus.FORBIDDEN);
        }
        if (accountsCurrent.size() == 3) {
            return new ResponseEntity<>("You can not have more than 3 accounts", HttpStatus.FORBIDDEN);
        }
        if (typeAccount.isEmpty()) {
            return new ResponseEntity<>("TypeAccount is empty", HttpStatus.FORBIDDEN);
        }
        if (!typeAccount.equals("AHORRO") && !typeAccount.equals("CORRIENTE")) {
            return new ResponseEntity<>("TypeAccount is invalid (only AHORRO or CORRIENTE)", HttpStatus.FORBIDDEN);
        }
        AccountType type;
        if (typeAccount.equals("AHORRO")) {
            type = AccountType.AHORRO;
        } else {
            type = AccountType.CORRIENTE;
        }
        if (accountsCurrent.size() <= 2) {
            long numberRandom = (long) (Math.random() * (100000000 - 1) + 1);
            String accountNumber = "VIN" + numberRandom;
            Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0, type);
            clientCurrent.addAccount(newAccount);
            accountService.saveAccount(newAccount);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PatchMapping("/api/clients/current/accounts")
    public ResponseEntity<?> deleteAccount(
            @RequestParam Long id,
            @RequestParam String password,
            Authentication authentication
    ) {
        Client clientCurrent = clientService.clientFindByEmail(authentication.getName());
        Set<Account> accountsCurrent = clientCurrent.getAccounts();

        if (clientCurrent == null) {
            return new ResponseEntity<>("You aren't an authenticated customer", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("The password is empty", HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(password, clientCurrent.getPassword())) {
            return new ResponseEntity<>("The password is incorrect", HttpStatus.FORBIDDEN);
        }
        if (id == null) {
            return new ResponseEntity<>("Id can't be 0", HttpStatus.FORBIDDEN);
        }
        if (id <= 0) {
            return new ResponseEntity<>("Id can't be null", HttpStatus.FORBIDDEN);
        }

        Account foundAccount = accountService.findAccountByID(id);

        if (foundAccount == null) {
            return new ResponseEntity<>("The id is not from any account", HttpStatus.FORBIDDEN);
        }
        if (!accountsCurrent.contains(foundAccount)) {
            return new ResponseEntity<>("The account is not your property", HttpStatus.FORBIDDEN);
        }
        if (foundAccount.getBalance() >= 1) {
            return new ResponseEntity<>("Account balance must be 0", HttpStatus.FORBIDDEN);
        }
        foundAccount.setAccount_EnabledOrDisabled(EnabledOrDisabled.DISABLED);
        accountService.saveAccount(foundAccount);

        transactionService.disableTransactionsAndSave(foundAccount);
        return new ResponseEntity<>(HttpStatus.OK);
    }




}

