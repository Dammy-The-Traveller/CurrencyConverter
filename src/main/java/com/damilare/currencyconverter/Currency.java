package com.damilare.currencyconverter;

public enum Currency {

    USD("US Dollar", "$"),
    GHS("Ghanaian Cedi", "₵"),
    EUR("Euro", "€"),
    GBP("British Pound", "£"),
    NGN("Nigerian Naira", "₦");

    private final String name;
    private final String symbol;

    Currency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }
}