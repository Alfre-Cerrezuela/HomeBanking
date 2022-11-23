package com.mindhub.homebanking.service.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.EnabledOrDisabled;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;
    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card findCardById(Long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }

    @Override
    public List<CardDTO> allCards() {
        return cardRepository.findAll().stream().filter(card -> card.getCard_EnabledOrDisabled() == EnabledOrDisabled.ENABLED)
                .map(cardEnable -> new CardDTO(cardEnable)).collect(Collectors.toList());
    }
    @Override
    public List<CardDTO> allCardsDisable(Client clientCurrent) {
        return clientCurrent.getCards().stream().filter(card -> card.getCard_EnabledOrDisabled() == EnabledOrDisabled.DISABLED)
                .map(cardDisable -> new CardDTO(cardDisable)).collect(Collectors.toList());
    }

    @Override
    public Card findCardbyNumber(String cardNumber) {
        return cardRepository.findByNumber(cardNumber);
    }
}
