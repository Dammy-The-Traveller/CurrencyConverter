// Declares the package folder path where this class file belongs
package com.damilare.currencyconverter;

// Imports BigDecimal for high-precision financial math calculations
import java.math.BigDecimal;

// Defines the public CurrencyConverter class responsible for handling mathematical currency conversions
public class CurrencyConverter {

    // Public method that multiplies the input amount by the exchange rate to get the final converted value
    public BigDecimal convert(BigDecimal amount, BigDecimal exchangeRate) {
        // Checks if the entered money amount is less than zero (negative value)
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            // Throws an exception stopping execution if a negative amount is passed
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        // Checks if the exchange rate is zero or negative (which would break division/multiplication rules)
        if (exchangeRate.compareTo(BigDecimal.ZERO) <= 0) {
            // Throws an exception stopping execution if the rate is invalid
            throw new IllegalArgumentException("Exchange rate must be greater than zero.");
        }

        // Multiplies the money amount by the exchange rate and returns the exact calculated BigDecimal result
        return amount.multiply(exchangeRate);
    }
}