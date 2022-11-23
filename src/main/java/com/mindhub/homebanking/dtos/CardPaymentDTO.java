package com.mindhub.homebanking.dtos;

public class CardPaymentDTO {
    private String cardNumber;
    private int cardCVV;
    private Double paymentAmount;
    private String paymentDescription;
    private String accountNumber;

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCardCVV() {
        return cardCVV;
    }

    public Double getPaymentAmount() {
        return paymentAmount;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}

