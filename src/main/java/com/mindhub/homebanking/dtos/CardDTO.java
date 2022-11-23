package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;

import java.time.LocalDate;

public class CardDTO {
    private long id;
    private CardType type;
    private String number;
    private int cvv;
    private CardColor color;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private String cardHolder;
    public CardDTO(Card card) {
            this.id = card.getId();
            this.type = card.getType();
            this.number = card.getNumber();
            this.cvv = card.getCvv();
            this.color = card.getColor();
            this.fromDate = card.getFromDate();
            this.thruDate = card.getThruDate();
            this.cardHolder = card.getCardHolder();
    }

    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public String getNumber() {
        return number;
    }

    public int getCvv() {
        return cvv;
    }

    public CardColor getColor() {
        return color;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getThruDate() {
        return thruDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

}
