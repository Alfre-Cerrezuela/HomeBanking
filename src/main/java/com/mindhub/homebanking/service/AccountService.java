package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;

import java.util.List;
import java.util.Set;

public interface AccountService {

    public Set<String> getAllAccountNumbers();

    public void saveAccount(Account account);

    public List<AccountDTO> getAllAccountsDTO();

    public AccountDTO FindAccountIDDTO(Long id);

    public Account findByNumber(String number);

    public  Account findAccountByID(long id);


}
