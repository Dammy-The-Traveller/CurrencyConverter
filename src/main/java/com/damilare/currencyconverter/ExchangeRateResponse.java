// Declares the package folder path where this class file belongs
package com.damilare.currencyconverter;

// Imports BigDecimal for high-precision decimal storage of exchange rates
import java.math.BigDecimal;

// Defines the ExchangeRateResponse class to hold JSON response data from the rate API
public class ExchangeRateResponse {

    // Declares a private string field to store the date of the exchange rate
    private String date;
    // Declares a private string field to store the base currency code
    private String base;
    // Declares a private string field to store the target quote currency code
    private String quote;
    // Declares a private BigDecimal field to store the numerical exchange rate value
    private BigDecimal rate;

    // Public getter method to retrieve the exchange rate date string
    public String getDate() {
        // Returns the date value
        return date;
    }

    // Public getter method to retrieve the base currency code
    public String getBase() {
        // Returns the base currency code string
        return base;
    }

    // Public getter method to retrieve the quote currency code
    public String getQuote() {
        // Returns the quote currency code string
        return quote;
    }

    // Public getter method to retrieve the numerical exchange rate value
    public BigDecimal getRate() {
        // Returns the exchange rate BigDecimal instance
        return rate;
    }
}