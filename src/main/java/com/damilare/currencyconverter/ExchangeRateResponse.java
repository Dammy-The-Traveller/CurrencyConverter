package com.damilare.currencyconverter;

import java.math.BigDecimal;

public class ExchangeRateResponse {

    private String date;
    private String base;
    private String quote;
    private BigDecimal rate;

    public String getDate() {
        return date;
    }

    public String getBase() {
        return base;
    }

    public String getQuote() {
        return quote;
    }

    public BigDecimal getRate() {
        return rate;
    }
}