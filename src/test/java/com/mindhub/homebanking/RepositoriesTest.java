//package com.mindhub.homebanking;
//
//
//import com.mindhub.homebanking.models.*;
//import com.mindhub.homebanking.repositories.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.util.Assert;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
//import java.util.List;
//
//import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;
//
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = NONE)
//public class RepositoriesTest {
////    @MockBean
////    PasswordEncoder passwordEncoder;
//    @Autowired
//    LoanRepository loanRepository;
//    @Autowired
//    AccountRepository accountRepository;
//    @Autowired
//    ClientRepository clientRepository;
//    @Autowired
//    CardRepository cardRepository;
//    @Autowired
//    TransactionRepository transactionRepository;
//
//    @Test
//    public void existLoans(){
//        List<Loan> loans = loanRepository.findAll();
//        assertThat(loans,is(not(empty())));
//
//    }
//    @Test
//    public void existPersonalLoan(){
//        List<Loan> loans = loanRepository.findAll();
//        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
//    }
//
//
//    @Test
//    public void existCards(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards,is(not(empty())));
//    }
//    @Test
//    public void existCardtypeDEBIT(){
//        List<Card> cards = cardRepository.findAll();
//        assertThat(cards, hasItem(hasProperty("type", is(CardType.DEBIT))));
//    }
//
//
//    @Test
//    public void existClients(){
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients,is(not(empty())));
//    }
//    @Test
//    public void existClientMelba(){
//        List<Client> clients = clientRepository.findAll();
//        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));
//    }
//
//
//    @Test
//    public void existAccount(){
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts,is(not(empty())));
//    }
//    @Test
//    public void existAccountVIN001(){
//        List<Account> accounts = accountRepository.findAll();
//        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));
//    }
//
//
//    @Test
//    public void existTransactions(){
//        List<Transaction> transactions = transactionRepository.findAll();
//        assertThat(transactions,is(not(empty())));
//    }
//    @Test
//    public void existTransactionCredit(){
//        List<Transaction> transactions = transactionRepository.findAll();
//        assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.CREDITO))));
//    }
//
//
//}
