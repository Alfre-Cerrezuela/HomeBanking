package com.mindhub.homebanking.models;

import com.lowagie.text.Cell;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id")
    private Account account;

    private TransactionType type;
    private Double amount;
    private String description;
    private LocalDateTime creationDate;
    private Double balance;
    private EnabledOrDisabled transacction_EnableOrDisable= EnabledOrDisabled.ENABLED;

    public Transaction() {
    }

    public Transaction(Account account, TransactionType type, double amount, String description, LocalDateTime creationDate) {
        this.account = account;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.creationDate = creationDate;
        this.balance = account.getBalance() + (amount);
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public EnabledOrDisabled getTransacction_EnableOrDisable() {
        return transacction_EnableOrDisable;
    }

    public void setTransacction_EnableOrDisable(EnabledOrDisabled transacction_EnableOrDisable) {
        this.transacction_EnableOrDisable = transacction_EnableOrDisable;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", account=" + account +
                ", type=" + type +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}
