package com.mindhub.homebanking.service;

import com.mindhub.homebanking.models.Loan;

import java.util.List;

public interface LoanService {
    public List<Loan> listLoans();

    public Loan findById(Long id);
    public void save(Loan loan);
    public Loan findByName(String name);
    public void  deleteLoanForId(long id);
}
