package com.mindhub.homebanking.controllers;


import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanCreateAdminDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    LoanService loanService;
    @Autowired
    ClientService clientService;
    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ClientLoanService clientLoanService;

    @GetMapping("/api/loans")
    public List<LoanDTO> getListLoanDTO() {
        List<Loan> listLoan = loanService.listLoans();
        List<LoanDTO> listLoanDTO = listLoan.stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
        return listLoanDTO;
    }

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<?> createRequestLoan(
            @RequestBody LoanApplicationDTO loanParam,
            Authentication authentication
    ) {
        Client clientCurrent = clientService.clientFindByEmail(authentication.getName());
        String accountNumberParam = loanParam.getNumberAccount();
        int paymentsParam = loanParam.getPayments();
        long idLoanParam = loanParam.getId();
        Double amountParam = loanParam.getAmount();
        Loan foundLoan = loanService.findById(idLoanParam);
        Account foundAccount = accountService.findByNumber(accountNumberParam);
        Set<Account> setAccounts = clientCurrent.getAccounts();
        Set<ClientLoan> clientLoans = clientCurrent.getLoans();

        if (clientCurrent == null) {
            return new ResponseEntity<>("You aren't an authenticated customer", HttpStatus.FORBIDDEN);
        }
        if (accountNumberParam.isEmpty()) {
            return new ResponseEntity<>("Account number is empty or invalid", HttpStatus.FORBIDDEN);
        }
        if (paymentsParam <= 0) {
            return new ResponseEntity<>("Payments number is empty or invalid", HttpStatus.FORBIDDEN);
        }
        if (idLoanParam <= 0) {
            return new ResponseEntity<>("Id loan number is empty or invalid", HttpStatus.FORBIDDEN);
        }
        if (amountParam.isNaN() || amountParam <= 0) {
            return new ResponseEntity<>("Amount number is empty or invalid", HttpStatus.FORBIDDEN);
        }
        if (foundLoan == null) {
            return new ResponseEntity<>("The requested loan does not exist", HttpStatus.FORBIDDEN);
        }
        if (foundLoan.getMaxAmount() < amountParam) {
            return new ResponseEntity<>("The requested amount is greater than the maximum amount", HttpStatus.FORBIDDEN);
        }
        if (foundAccount == null) {
            return new ResponseEntity<>("The selected account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!setAccounts.contains(foundAccount)) {
            return new ResponseEntity<>("The account you select is not yours", HttpStatus.FORBIDDEN);
        }
        if (clientLoans.stream().filter(clientLoan -> clientLoan.getLoan().getName().equals(foundLoan.getName())).toArray().length == 1) {
            return new ResponseEntity<>("You can't have two loans of the same type at the same time", HttpStatus.FORBIDDEN);
        }

        String nameLoan = foundLoan.getName();

        double amountPorcentage = amountParam * foundLoan.getPercentage();

        Transaction newTransaction = new Transaction(foundAccount, TransactionType.CREDITO, amountParam, foundLoan.getName() + " loan approved", LocalDateTime.now());
        transactionService.saveTreansaction(newTransaction);

        foundAccount.setBalance(foundAccount.getBalance() + amountParam);
        accountService.saveAccount(foundAccount);

        ClientLoan newClientLoan = new ClientLoan(amountPorcentage, paymentsParam, clientCurrent, foundLoan);
        clientLoanService.saveClientLoan(newClientLoan);

        return new ResponseEntity<>("", HttpStatus.CREATED);
    }


    @PostMapping("/api/loans/create")
    public ResponseEntity<?> createLoan(
            @RequestBody LoanCreateAdminDTO loan,
            Authentication authentication
    ) {
        Client adminCurrent = clientService.clientFindByEmail(authentication.getName());
        String loanName = loan.getName();
        Double loanMaxAmount = loan.getMaxAmount();
        List<Integer> loanPayments = loan.getPayments();
        Double loanPercentage = loan.getPercentage();

        if (adminCurrent == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (loanName.isEmpty()) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
//        if (loanService.findByName(loanName) != null){
//            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
//        }
        if (loanMaxAmount.isNaN() || loanMaxAmount == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (loanMaxAmount < 1) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (loanPayments == null || loanPayments.size() == 0) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (loanPercentage.isNaN() || loanPercentage == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (loanPercentage < 0) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }

        Loan creatingLoan = new Loan(loanName, loanMaxAmount, loanPayments, loanPercentage);
        loanService.save(creatingLoan);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PutMapping("/api/loans/put")
    public ResponseEntity<?> putLoan(
            Authentication authentication,
            @RequestParam Long id,
            @RequestParam String nameLoan,
            @RequestParam Double maxAmountLoan,
            @RequestParam List<Integer> paymentsLoan,
            @RequestParam Double percentageLoan
    ){
        Client adminCurrent = clientService.clientFindByEmail(authentication.getName());

        if (adminCurrent == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (id == null || id < 1){
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (nameLoan.isEmpty()) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
//        if (loanService.findByName(loanName) != null){
//            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
//        }
        if (maxAmountLoan.isNaN() || maxAmountLoan == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (maxAmountLoan < 1) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (paymentsLoan == null || paymentsLoan.size() == 0) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (percentageLoan.isNaN() || percentageLoan == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (percentageLoan < 0) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }

        Loan foundLoan = loanService.findById(id);

        if (foundLoan == null){
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        foundLoan.setName(nameLoan);
        foundLoan.setMaxAmount(maxAmountLoan);
        foundLoan.setPayments(paymentsLoan);
        foundLoan.setPercentage(percentageLoan);
        loanService.save(foundLoan);
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @DeleteMapping("/api/loans/delete")
    public ResponseEntity<?> borrarPrestamo(
            Authentication authentication,
            @RequestParam Long id
    ) {
        Client adminCurrent = clientService.clientFindByEmail(authentication.getName());
        if (adminCurrent == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (id < 1) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (id == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        loanService.deleteLoanForId(id);
        return new ResponseEntity<>("", HttpStatus.OK);
    }


}




