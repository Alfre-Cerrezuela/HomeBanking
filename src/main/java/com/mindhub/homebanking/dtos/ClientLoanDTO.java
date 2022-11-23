package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
    private long id;
    private double amount;
    private double payments;
    private long id_Loan;
    private String name;
    private Double payment;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id= clientLoan.getId();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.id_Loan = clientLoan.getLoan().getId();
        this.name = clientLoan.getLoan().getName();
        this.payment = clientLoan.getPayment();
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public double getPayments() {
        return payments;
    }

    public long getId_Loan() {
        return id_Loan;
    }

    public String getName() {
        return name;
    }

    public Double getPayment() {
        return payment;
    }
}
