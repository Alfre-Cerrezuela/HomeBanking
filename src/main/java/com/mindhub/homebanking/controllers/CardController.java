package com.mindhub.homebanking.controllers;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.CardService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static com.mindhub.homebanking.utils.CardUtils.*;

@RestController
public class CardController {

    @Autowired
    CardService cardService;
    @Autowired
    ClientService clientService;
    private Card newCard;


    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<?> createAccount(Authentication authentication, @RequestParam CardColor cardColor,
                                           @RequestParam CardType cardType, @RequestParam String visa_mastercard) {

        Client clientCurrent = clientService.clientFindByEmail(authentication.getName());
        Set<Card> cards = clientCurrent.getCards().stream().filter(card -> card.getType() == cardType &&
                card.getCard_EnabledOrDisabled() == EnabledOrDisabled.ENABLED).collect(Collectors.toSet());

        if (clientCurrent == null) {
            return new ResponseEntity<>("You aren't an authenticated customer", HttpStatus.FORBIDDEN);
        }
        if (cards.size() == 3) {
            return new ResponseEntity<>("You can't have more than 3 cards of the same type", HttpStatus.FORBIDDEN);
        }
        if (cards.stream().filter(card -> card.getColor() == cardColor).toArray().length == 1) {
            return new ResponseEntity<>("You can't have more than 1 cards of the same color", HttpStatus.FORBIDDEN);
        }
        if (cardColor == null) {
            return new ResponseEntity<>("card color is invalid (is null)", HttpStatus.FORBIDDEN);
        }
        if (cardType == null) {
            return new ResponseEntity<>("card type is invalid (is null)", HttpStatus.FORBIDDEN);
        }
        if (visa_mastercard == null) {
            return new ResponseEntity<>("card visa or mastercard is invalid (is null)", HttpStatus.FORBIDDEN);
        }
        if (cardColor != CardColor.GOLD && cardColor != CardColor.TITANIUM && cardColor != CardColor.SILVER) {
            return new ResponseEntity<>("card color is invalid (only SILVER, TITANIUM or GOLD)", HttpStatus.FORBIDDEN);
        }
        if (cardType != CardType.DEBIT && cardType != CardType.CREDIT) {
            return new ResponseEntity<>("card type is invalid (only DEBIT or DEBIT)", HttpStatus.FORBIDDEN);
        }
        if (!visa_mastercard.equals("VISA") && !visa_mastercard.equals("MASTERCARD")) {
            return new ResponseEntity<>("card Visa or card MasterCard is invalid (only VISA or MASTERCARD)", HttpStatus.FORBIDDEN);
        }

        if (cards.size() <= 2) {
            Card newCard;
            if (visa_mastercard.equals("VISA")) {
                newCard = new Card(cardType, getCardNumberVISA(),getCardNumberCVV() , cardColor, LocalDate.now(),
                        LocalDate.now().plusYears(5), clientCurrent);
            } else {
                newCard = new Card(cardType, getCardNumberMasterCard(), getCardNumberCVV(), cardColor, LocalDate.now(), LocalDate.now().plusYears(5), clientCurrent);
            }

            cardService.saveCard(newCard);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @PatchMapping("/api/clients/current/cards/delete")
    public ResponseEntity<?> deleteCard(
            @RequestParam Long id,
            Authentication authentication
    ){
        Client clientCurrent= clientService.clientFindByEmail(authentication.getName());
        Set<Card> cardsClientCurrent = clientCurrent.getCards();

        if (clientCurrent == null){
            return new ResponseEntity<>("You aren't an authenticated customer", HttpStatus.FORBIDDEN);
        }
        if (id == null || id==0){
            return new ResponseEntity<>("Id can't be 0 or null", HttpStatus.FORBIDDEN);
        }
        Card foundCard = cardService.findCardById(id);
        if (foundCard == null){
            return new ResponseEntity<>("The id is not from any card", HttpStatus.FORBIDDEN);
        }
        if (!cardsClientCurrent.contains(foundCard)){
            return new ResponseEntity<>("The card is not your property", HttpStatus.FORBIDDEN);
        }

        foundCard.setCard_EnabledOrDisabled(EnabledOrDisabled.DISABLED);
        cardService.saveCard(foundCard);
        return new ResponseEntity<>("e", HttpStatus.OK);
    }


    @GetMapping("/api/clients/current/cards/disable")
    public List<CardDTO> cardsDisable (Authentication authentication){
        Client clientCurrent = clientService.clientFindByEmail(authentication.getName());
        return cardService.allCardsDisable(clientCurrent);
    } 
}

