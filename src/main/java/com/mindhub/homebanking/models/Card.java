package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name="native", strategy = "native")
    private long id;
    private CardType type;
    private String number;
    private int cvv;
    private CardColor color;
    private LocalDate fromDate;
    private LocalDate thruDate;
    private EnabledOrDisabled card_EnabledOrDisabled = EnabledOrDisabled.ENABLED;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private Client clientCard;
    private String cardHolder;

    public Card() {
    }

    public Card(CardType type, String number, int cvv, CardColor color, LocalDate fromDate, LocalDate thruDate, Client clientCard) {
        this.type = type;
        this.number = number;
        this.cvv = cvv;
        this.color = color;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.clientCard = clientCard;
        this.cardHolder = clientCard.getFirstName() + " " + clientCard.getLastName();
    }


    public long getId() {
        return id;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public CardColor getColor() {
        return color;
    }

    public void setColor(CardColor color) {
        this.color = color;
    }
    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }
    public LocalDate getThruDate() {
        return thruDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    @JsonIgnore
    public Client getClientCard() {
        return clientCard;
    }

    public void setClientCard(Client clientCard) {
        this.clientCard = clientCard;
    }
    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public EnabledOrDisabled getCard_EnabledOrDisabled() {return card_EnabledOrDisabled;}

    public void setCard_EnabledOrDisabled(EnabledOrDisabled card_EnabledOrDisabled) {this.card_EnabledOrDisabled = card_EnabledOrDisabled;}
}
