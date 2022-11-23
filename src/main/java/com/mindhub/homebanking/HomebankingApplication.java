package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.event.ListDataListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class HomebankingApplication {


	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);

	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository,
									  TransactionRepository transactionRepository, LoanRepository loanRepository,
									  ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return  args -> {
			String token = UUID.randomUUID().toString();


			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));

			Client client2= new Client("Alfre", "Cerrezuela", "alfredodcerrezuela@gmail.com", passwordEncoder.encode("alfre123"));

			Client clientADMIN = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("admin123"));

			Account account1 = new Account("VIN001", LocalDateTime.now(),750000, AccountType.AHORRO);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1),7500, AccountType.CORRIENTE);
			Account account3 = new Account("VIN003", LocalDateTime.now(),12005, AccountType.AHORRO);

			Account account4 = new Account("VIN004", LocalDateTime.now(),750000,AccountType.CORRIENTE);
			Account account5 = new Account("VIN005", LocalDateTime.now().plusDays(1),7500,AccountType.CORRIENTE);
			Account account6 = new Account("VIN006", LocalDateTime.now(),12005,AccountType.AHORRO);



			clientRepository.save(client1);
			clientRepository.save(client2);
			clientRepository.save(clientADMIN);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client1.addAccount(account3);

			client2.addAccount(account4);
			client2.addAccount(account5);
			client2.addAccount(account6);


			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			accountRepository.save(account4);
			accountRepository.save(account5);
			accountRepository.save(account6);


			Transaction transaction1= new Transaction(account1, TransactionType.CREDITO, 32000,"para el litro de nafta",LocalDateTime.now());
			Transaction transaction2= new Transaction(account1, TransactionType.DEBITO, -2000, "para el fernet con coca",LocalDateTime.now());
			Transaction transaction3= new Transaction(account1, TransactionType.DEBITO, -500, "para los puchos", LocalDateTime.now());

			Transaction transaction4= new Transaction(account2, TransactionType.CREDITO, 32000,"para el litro de nafta",LocalDateTime.now());
			Transaction transaction5= new Transaction(account2, TransactionType.DEBITO, -2000, "para el fernet con coca",LocalDateTime.now());
			Transaction transaction6= new Transaction(account2, TransactionType.DEBITO, -500, "para los puchos", LocalDateTime.now());

			Transaction transaction7= new Transaction(account3, TransactionType.CREDITO, 32000,"para el litro de nafta",LocalDateTime.now());
			Transaction transaction8= new Transaction(account3, TransactionType.DEBITO, -2000, "para el fernet con coca",LocalDateTime.now());
			Transaction transaction9= new Transaction(account3, TransactionType.DEBITO, -500, "para los puchos", LocalDateTime.now());


			Transaction transaction10= new Transaction(account4, TransactionType.CREDITO, 32000,"para el litro de nafta",LocalDateTime.now());
			Transaction transaction11= new Transaction(account4, TransactionType.DEBITO, -2000, "para el fernet con coca",LocalDateTime.now());
			Transaction transaction12= new Transaction(account4, TransactionType.DEBITO, -500, "para los puchos", LocalDateTime.now());

			Transaction transaction13= new Transaction(account5, TransactionType.CREDITO, 32000,"para el litro de nafta",LocalDateTime.now());
			Transaction transaction14= new Transaction(account5, TransactionType.DEBITO, -2000, "para el fernet con coca",LocalDateTime.now());
			Transaction transaction15= new Transaction(account5, TransactionType.DEBITO, -500, "para los puchos", LocalDateTime.now());

			Transaction transaction16= new Transaction(account6, TransactionType.CREDITO, 32000,"para el litro de nafta",LocalDateTime.now());
			Transaction transaction17= new Transaction(account6, TransactionType.DEBITO, -2000, "para el fernet con coca",LocalDateTime.now());
			Transaction transaction18= new Transaction(account6, TransactionType.DEBITO, -500, "para los puchos", LocalDateTime.now());

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);
			transactionRepository.save(transaction9);

			transactionRepository.save(transaction10);
			transactionRepository.save(transaction11);
			transactionRepository.save(transaction12);
			transactionRepository.save(transaction13);
			transactionRepository.save(transaction14);
			transactionRepository.save(transaction15);
			transactionRepository.save(transaction16);
			transactionRepository.save(transaction17);
			transactionRepository.save(transaction18);

			Loan loan1 = new Loan("Hipotecario", 500000,new ArrayList<>(List.of(12, 24, 36, 48, 60)), 1.68);
			Loan loan2 = new Loan("Personal", 100000,new ArrayList<>(List.of(6 , 12, 24)), 1.43);
			Loan loan3 = new Loan("Automotriz", 300000,new ArrayList<>(List.of(6, 12, 24, 36)), 1.56);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			ClientLoan clientLoan1= new ClientLoan( 400000, 60, client1, loan1);
			ClientLoan clientLoan2= new ClientLoan( 50000, 12, client1, loan2);

			ClientLoan clientLoan3= new ClientLoan( 100000, 24, client2, loan2);
			ClientLoan clientLoan4= new ClientLoan( 200000, 36, client2, loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			Card card1= new Card(CardType.DEBIT,"5234-5234-5234-5234", 123, CardColor.GOLD, LocalDate.now(), LocalDate.now().plusYears(5), client1);
			Card card2= new Card(CardType.CREDIT, "4321-4321-4321-4321", 321, CardColor.TITANIUM, LocalDate.now(), LocalDate.now().plusYears(5), client1);
			Card card3= new Card(CardType.CREDIT, "5678-5678-5678-5678", 567, CardColor.SILVER, LocalDate.now(), LocalDate.now().plusYears(5), client2);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

		};
	}
}
