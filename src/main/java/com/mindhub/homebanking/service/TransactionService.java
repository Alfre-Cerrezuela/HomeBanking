package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDateTime;
import java.util.Set;

public interface TransactionService {
    public void saveTreansaction(Transaction transaction);
    public void disableTransactionsAndSave(Account account);
    public Set<Transaction> transactionFilter(LocalDateTime from, LocalDateTime to, Set<Transaction> transactions);
}
