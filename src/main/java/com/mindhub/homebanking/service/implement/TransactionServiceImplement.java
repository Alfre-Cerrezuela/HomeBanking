package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.EnabledOrDisabled;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImplement implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    AccountService accountService;
    @Override
    public void saveTreansaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    @Override
    public void disableTransactionsAndSave(Account account) {
         account.getTransactions().stream().forEach(transaction ->
                transaction.setTransacction_EnableOrDisable(EnabledOrDisabled.DISABLED));
         accountService.saveAccount(account);
    }
    @Override
    public Set<Transaction> transactionFilter(LocalDateTime from, LocalDateTime to, Set<Transaction> transactions) {
        return transactions.stream().filter(transaction ->(transaction.getCreationDate()).isAfter(from)  && (transaction.getCreationDate()).isBefore(to)).collect(Collectors.toSet());
    }
}
