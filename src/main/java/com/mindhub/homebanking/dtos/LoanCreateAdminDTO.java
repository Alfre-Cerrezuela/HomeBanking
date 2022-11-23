package com.mindhub.homebanking.dtos;

import java.util.List;

public class LoanCreateAdminDTO {
    private String name;
    private double maxAmount;
    private List<Integer> payments;
    private Double percentage;

    public String getName() {
        return name;
    }
    public double getMaxAmount() {
        return maxAmount;
    }
    public List<Integer> getPayments() {
        return payments;
    }
    public Double getPercentage() {
        return percentage;
    }
}
