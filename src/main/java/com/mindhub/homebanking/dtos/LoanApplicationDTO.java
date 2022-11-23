package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long idLoan;
    private Double amount;
    private int payments;
    private String numberAccount;


    public LoanApplicationDTO(long idLoan, Double amount, int payment, String numberAccount) {
        this.idLoan = idLoan;
        this.amount = amount;
        this.payments = payment;
        this.numberAccount = numberAccount;
    }

    public long getId() {
        return idLoan;
    }

    public Double getAmount() {
        return amount;
    }
    public int getPayments() {
        return payments;
    }
    public String getNumberAccount() {
        return numberAccount;
    }
}


