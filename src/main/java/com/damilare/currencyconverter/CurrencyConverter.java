package com.damilare.currencyconverter;

import java.math.BigDecimal;

public class CurrencyConverter {

    public BigDecimal convert(BigDecimal amount, BigDecimal exchangeRate) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        if (exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Exchange rate must be greater than zero.");
        }

        return amount.multiply(exchangeRate);
    }
}