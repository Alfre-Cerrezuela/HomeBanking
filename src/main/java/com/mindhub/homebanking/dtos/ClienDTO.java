package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.EnabledOrDisabled;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClienDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private  Set<AccountDTO> accounts = new HashSet<>();
    private List<ClientLoanDTO> loans= new ArrayList<>();
    private List<CardDTO> cards= new ArrayList<>();


    public ClienDTO() {
    }

    public ClienDTO(Client client){
      this.id  = client.getId();
      this.firstName = client.getFirstName();
      this.lastName = client.getLastName();
      this.email = client.getEmail();
      this.password= client.getPassword();
      this.accounts = client.getAccounts().stream().filter(account -> account.getAccount_EnabledOrDisabled() == EnabledOrDisabled.ENABLED)
              .map(accountEnable -> new AccountDTO(accountEnable)).collect(Collectors.toSet());
      this.loans= client.getLoans().stream().map(loan -> new ClientLoanDTO(loan)).collect(Collectors.toList());
      this.cards= client.getCards().stream().filter(card -> card.getCard_EnabledOrDisabled() == EnabledOrDisabled.ENABLED)
              .map(cardEnable -> new CardDTO(cardEnable)).collect(Collectors.toList());
    };

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {return accounts;}

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public List<CardDTO> getCards() {
        return cards;
    }

    public String getPassword() {
        return password;
    }
}
