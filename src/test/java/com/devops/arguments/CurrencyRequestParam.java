package com.devops.arguments;

public class CurrencyRequestParam {
    private String fromCurrency;
    private String toCurrency;
    private Double amount;

    public CurrencyRequestParam(String fromCurrency, String toCurrency, Double amount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public Double getAmount() {
        return amount;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
