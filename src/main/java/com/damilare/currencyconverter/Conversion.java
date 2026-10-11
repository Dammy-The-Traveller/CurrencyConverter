// Package declaration matching the project directory structure
package com.damilare.currencyconverter;

// Imports the BigDecimal class for high-precision monetary calculations
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Defines the Conversion class to store details of an executed currency conversion transaction
public class Conversion {

    // Defines a static formatter constant for "yyyy-MM-dd HH:mm" output format
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Declares immutable field for the original conversion amount
    private final BigDecimal amount;
    // Declares immutable field for the source currency
    private final Currency from;
    // Declares immutable field for the target currency
    private final Currency to;
    // Declares immutable field for the applied exchange rate
    private final BigDecimal exchangeRate;
    // Declares immutable field for the final converted result amount
    private final BigDecimal result;
    // Declares immutable field holding the exact local timestamp when conversion occurred
    private final LocalDateTime timestamp;

    // Constructor to initialize conversion record fields and record current timestamp
 
public Conversion(
        BigDecimal amount,
        Currency from,
        Currency to,
        BigDecimal exchangeRate,
        BigDecimal result
) {
    this(amount, from, to, exchangeRate, result, LocalDateTime.now());
}

public Conversion(
        BigDecimal amount,
        Currency from,
        Currency to,
        BigDecimal exchangeRate,
        BigDecimal result,
        LocalDateTime timestamp
) {
    this.amount = amount;
    this.from = from;
    this.to = to;
    this.exchangeRate = exchangeRate;
    this.result = result;
    this.timestamp = timestamp;
}

    // Getter method returning the conversion original amount
    public BigDecimal getAmount() {
        // Returns the original input amount
        return amount;
    }

    // Getter method returning the source currency
    public Currency getFrom() {
        // Returns the source currency
        return from;
    }

    // Getter method returning the target currency
    public Currency getTo() {
        // Returns the target currency
        return to;
    }

    // Getter method returning the exchange rate used
    public BigDecimal getExchangeRate() {
        // Returns the exchange rate
        return exchangeRate;
    }

    // Getter method returning the final converted result
    public BigDecimal getResult() {
        // Returns the converted result amount
        return result;
    }

    // Getter method returning the raw LocalDateTime timestamp object
    public LocalDateTime getTimestamp() {
        // Returns the unformatted LocalDateTime instance
        return timestamp;
        
    }

    // Returns the timestamp formatted strictly as "yyyy-MM-dd HH:mm" (e.g., "2026-10-10 15:45")
    public String getFormattedTimestamp() {
        // Applies the custom pattern to format the LocalDateTime instance into a clean String
        return timestamp.format(FORMATTER);
    }
}