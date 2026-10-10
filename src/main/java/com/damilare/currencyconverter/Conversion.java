package com.damilare.currencyconverter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Conversion {

    private final BigDecimal amount;
    private final Currency from;
    private final Currency to;
    private final BigDecimal exchangeRate;
    private final BigDecimal result;
    private final LocalDateTime timestamp;

    public Conversion(
            BigDecimal amount,
            Currency from,
            Currency to,
            BigDecimal exchangeRate,
            BigDecimal result
    ) {
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.exchangeRate = exchangeRate;
        this.result = result;
        this.timestamp = LocalDateTime.now();
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getFrom() {
        return from;
    }

    public Currency getTo() {
        return to;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public BigDecimal getResult() {
        return result;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}