package com.mindhub.homebanking.controllers;

import com.lowagie.text.DocumentException;
import com.mindhub.homebanking.dtos.CardPaymentDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
public class TransactionController {

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;
    @Autowired
    ClientService clientService;
    @Autowired
    PDFService pdfService;
    @Autowired
    CardService cardService;

    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<?> createTransaction(
            @RequestParam Double amount,
            @RequestParam String description,
            @RequestParam String numberSourceAccount,
            @RequestParam String numberDestinationAccount,
            Authentication authentication
    ) {
        Client clientCurrent = clientService.clientFindByEmail(authentication.getName());

        Account sourceAccount = accountService.findByNumber(numberSourceAccount);

        Account destinationAccount = accountService.findByNumber(numberDestinationAccount);

        Set<Account> accountsClientCurrent = clientCurrent.getAccounts();

        if (clientCurrent == null) {
            return new ResponseEntity<>("You aren't an authenticated customer", HttpStatus.FORBIDDEN);
        }

        if (amount.isNaN() || amount <= 0) {
            return new ResponseEntity<>("Amount isn't valid or is equal to $0.0", HttpStatus.FORBIDDEN);
        }
        ;
        if (description.isEmpty()) {
            return new ResponseEntity<>("Description isn't valid or is empty.", HttpStatus.FORBIDDEN);
        }
        ;
        if (numberSourceAccount.isEmpty()) {
            return new ResponseEntity<>("The number of the source isn't valid or is empty.", HttpStatus.FORBIDDEN);
        }
        ;
        if (numberDestinationAccount.isEmpty()) {
            return new ResponseEntity<>("The number of the destination isn't valid or is empty.", HttpStatus.FORBIDDEN);
        }
        ;
        if (numberSourceAccount.equals(numberDestinationAccount)) {
            return new ResponseEntity<>("The number of the destination account doesn't have to be equal to source account number", HttpStatus.FORBIDDEN);
        }
        ;
        if (sourceAccount == null) {
            return new ResponseEntity<>("The source account not exist.", HttpStatus.FORBIDDEN);
        }
        ;
        if (!accountsClientCurrent.contains(sourceAccount)) {
            return new ResponseEntity<>("The source account isn't one of your accounts.", HttpStatus.FORBIDDEN);
        }
        ;
        if (destinationAccount == null) {
            return new ResponseEntity<>("The destination account not exist.", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount.getBalance() < amount) {
            return new ResponseEntity<>("The selected account does not have the available balance.", HttpStatus.FORBIDDEN);
        }
        ;


        double balanceSourceAccount = sourceAccount.getBalance() - amount;
        double balanceDestinationAccount = destinationAccount.getBalance() + amount;

        sourceAccount.setBalance(balanceSourceAccount);
        destinationAccount.setBalance(balanceDestinationAccount);

        accountService.saveAccount(sourceAccount);
        accountService.saveAccount(destinationAccount);

        Transaction sourceTransaction = new Transaction(sourceAccount, TransactionType.DEBITO, -amount, description + " " + numberDestinationAccount, LocalDateTime.now());
        Transaction destinationTransaction = new Transaction(destinationAccount, TransactionType.CREDITO, amount, description + " " + numberSourceAccount, LocalDateTime.now());


        transactionService.saveTreansaction(sourceTransaction);
        transactionService.saveTreansaction(destinationTransaction);

        return new ResponseEntity<>("The transaction was completed successfully", HttpStatus.CREATED);

    }

    ;
    LocalDateTime localDateTimeNow = LocalDateTime.now();
    LocalDateTime from = null;
    String accountNumberFinal;

    @PostMapping("/api/pdf")
    public ResponseEntity<?> requestPDF(
            @RequestParam String accountNumber,
            @RequestParam String fromString,
            Authentication authentication
    ) {
        Client current = clientService.clientFindByEmail(authentication.getName());
        if (current == null) {
            return new ResponseEntity<>("a", HttpStatus.FORBIDDEN);
        }
        if (accountNumber.isEmpty()) {
            return new ResponseEntity<>("b", HttpStatus.FORBIDDEN);
        }
        if (fromString.isEmpty()) {
            return new ResponseEntity<>("c", HttpStatus.FORBIDDEN);
        }
        accountNumberFinal = accountNumber;
        if (fromString.equals("semana")) {
            from = LocalDateTime.now().minusDays(7);
        }
        if (fromString.equals("mes")) {
            from = LocalDateTime.now().minusMonths(1);
        }
        if (fromString.equals("semestre")) {
            from = LocalDateTime.now().minusMonths(6);
        }
        if (fromString.equals("año")) {
            from = LocalDateTime.now().minusYears(1);
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @GetMapping("/api/pdf")
    public void pdf(
            HttpServletResponse response) throws DocumentException, IOException {

        Account account = accountService.findByNumber(accountNumberFinal);
        Set<Transaction> transactions = account.getTransactions();
        Set<Transaction> transaccionesFiltradas = transactionService.transactionFilter(from, localDateTimeNow, transactions);
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValues = "attachment; filename=pdf_Transacciones.pdf";
        response.setHeader(headerKey, headerValues);
        pdfService.export(account, transaccionesFiltradas, response);

    }

    @Transactional
    @PostMapping("/api/transactions/card")
    public ResponseEntity<?> paymentWithCard(
            Authentication authentication,
            @RequestBody CardPaymentDTO cardPayment
    ) {
        Client clientCurrent = clientService.clientFindByEmail(authentication.getName());
        String cardNumber = cardPayment.getCardNumber();
        Integer cardCVV = cardPayment.getCardCVV();
        Double paymentAmount = cardPayment.getPaymentAmount();
        String paymentDescription = cardPayment.getPaymentDescription();
        String accountNumber = cardPayment.getAccountNumber();
        Account foundAccount = accountService.findByNumber(accountNumber);
        Card foundCard = cardService.findCardbyNumber(cardNumber);
        if (clientCurrent == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (cardNumber.isEmpty()) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (cardCVV > 999 || cardCVV < 1) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (paymentAmount < 1 || paymentAmount.isNaN()) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (foundAccount == null) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (foundAccount.getBalance() < paymentAmount) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (paymentDescription.isEmpty()) {
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (foundCard == null){
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        if (foundCard.getThruDate().isAfter(LocalDate.now())){
            return new ResponseEntity<>("", HttpStatus.FORBIDDEN);
        }
        Transaction newTransaction = new Transaction(foundAccount,TransactionType.DEBITO,paymentAmount,paymentDescription,LocalDateTime.now());
        transactionService.saveTreansaction(newTransaction);

        foundAccount.setBalance(foundAccount.getBalance() - paymentAmount);
        accountService.saveAccount(foundAccount);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
