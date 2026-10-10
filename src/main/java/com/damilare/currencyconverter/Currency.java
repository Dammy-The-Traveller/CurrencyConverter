
package com.damilare.currencyconverter;

import java.util.List;
import java.util.Objects;

public final class Currency {

    public static final Currency USD = new Currency("USD", "US Dollar", "$");
    public static final Currency GHS = new Currency("GHS", "Ghanaian Cedi", "₵");
    public static final Currency EUR = new Currency("EUR", "Euro", "€");
    public static final Currency GBP = new Currency("GBP", "British Pound", "£");
    public static final Currency NGN = new Currency("NGN", "Nigerian Naira", "₦");

    private final String code;
    private final String name;
    private final String symbol;

    public Currency(String code, String name, String symbol) {
        if (code == null || !code.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency code must contain three uppercase letters.");
        }

        this.code = code;
        this.name = Objects.requireNonNull(name, "Currency name cannot be null.");
        this.symbol = symbol == null || symbol.isBlank() ? code : symbol;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public static List<Currency> fallbackCurrencies() {
        return List.of(USD, GHS, EUR, GBP, NGN);
    }

    @Override
    public String toString() {
        return code;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Currency currency)) {
            return false;
        }

        return code.equals(currency.code);
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }
}