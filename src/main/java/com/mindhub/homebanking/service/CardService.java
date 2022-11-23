package com.mindhub.homebanking.service;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface CardService {
    public void saveCard(Card card);
    public  Card findCardById(Long id);
    public void deleteCard(Long id);
    public List<CardDTO> allCards();
    public List<CardDTO> allCardsDisable(Client clientCurrent);
    public  Card findCardbyNumber(String cardNumber);

}
