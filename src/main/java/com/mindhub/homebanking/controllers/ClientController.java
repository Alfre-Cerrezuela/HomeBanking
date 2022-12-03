package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClienDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    EmailService emailService;


    @GetMapping("/api/clients")
    public List<ClienDTO> getClients() {
        return clientService.getClientsDTO();
    };

    @GetMapping("/api/clients/{id}")
    public ClienDTO getIdClient(@PathVariable Long id) {
        return clientService.getIdClientDTO(id);
    }

    @PostMapping("/api/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {

        if (firstName.isEmpty()) {
            return new ResponseEntity<>("Missing firstName", HttpStatus.FORBIDDEN);
        }
        if (lastName.isEmpty()) {
            return new ResponseEntity<>("Missing lastName", HttpStatus.FORBIDDEN);
        }
        if (email.isEmpty()) {
            return new ResponseEntity<>("Missing email", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()) {
            return new ResponseEntity<>("Missing password", HttpStatus.FORBIDDEN);
        }

        if (clientService.clientFindByEmail(email) != null) {
            return new ResponseEntity<>("Email already in use", HttpStatus.FORBIDDEN);
        }
        Client newClient = new Client(firstName, lastName, email, passwordEncoder.encode(password));

        clientService.saveClient(newClient);

        Set<String> accountsNumber = accountService.getAllAccountNumbers();

        long numberRandom;

        do {
            numberRandom = (long) (Math.random() * (100000000 - 1) + 1);

        }while (accountsNumber.equals("VIN" + numberRandom));

        String accountNumber = "VIN" + numberRandom;

        Account newAccount = new Account(accountNumber, LocalDateTime.now(), 0, AccountType.CORRIENTE);

        newClient.addAccount(newAccount);

        accountService.saveAccount(newAccount);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }


    @GetMapping("/api/clients/current")
    public ClienDTO getIdClientCurrent(Authentication authentication) {
        Client clientCurrent = clientService.clientFindByEmail(authentication.getName());
//        Object cosas= authentication.getCredentials();
        return new ClienDTO(clientCurrent);
    }
    @PostMapping("/api/client/mail")
    public ResponseEntity<?> sendEmailTool(
            @RequestParam String to,
            @RequestParam int code
    ){
        String textMenssage = "El codigo de validacion es: " + code;
        String subject = "Validación del Email: " +to+". Para registrarse en UnityBank";
        emailService.sendEmailTool(textMenssage,to,subject);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PatchMapping("/api/client/validation")
    public ResponseEntity<?> validationOfEmail(
            @RequestParam String email,
            @RequestParam int code
    ){
        Client clientCurrent = clientService.clientFindByEmail(email);
        if (code == clientCurrent.getCodeOfValidation()){
            clientCurrent.setValidated(true);
            clientService.saveClient(clientCurrent);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
