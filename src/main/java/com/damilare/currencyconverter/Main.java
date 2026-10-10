package com.damilare.currencyconverter;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Main {

    public static void main(String[] args) {

        CurrencyConverter converter = new CurrencyConverter();
        ExchangeRateService exchangeRateService = new ExchangeRateService();
        InputHandler inputHandler = new InputHandler();
        ConversionHistory history = new ConversionHistory();

        boolean running = true;

        while (running) {

            System.out.println();
            System.out.println("========================================");
            System.out.println("          CURRENCY CONVERTER");
            System.out.println("========================================");
            System.out.println("1. Convert Currency");
            System.out.println("2. Conversion History");
            System.out.println("3. Exit");
            System.out.println("========================================");

            int choice = inputHandler.getMenuChoice();

            switch (choice) {

                case 1 -> {

                  Currency from = inputHandler.getCurrency(
        "Select source currency:"
);

Currency to = inputHandler.getCurrency(
        "Select target currency:"
);

                    BigDecimal amount = inputHandler.getAmount();

                    try {

                        BigDecimal rate = exchangeRateService.getRate(from, to);

                        BigDecimal result = converter.convert(amount, rate);

                        Conversion conversion = new Conversion(
                                amount,
                                from,
                                to,
                                rate,
                                result
                        );

                        history.addConversion(conversion);

                        System.out.println();
                        System.out.println("----------------------------------------");

                        System.out.printf(
                                "%s %s = %s %s%n",
                                amount.setScale(2, RoundingMode.HALF_UP),
                                from,
                                result.setScale(2, RoundingMode.HALF_UP),
                                to
                        );

                        System.out.printf(
                                "Exchange Rate: 1 %s = %s %s%n",
                                from,
                                rate.setScale(4, RoundingMode.HALF_UP),
                                to
                        );

                        System.out.println("----------------------------------------");

                    } catch (RuntimeException e) {

    System.out.println();
    System.out.println("Conversion failed: " + e.getMessage());
}
                }

                case 2 -> {

                    System.out.println();
                    System.out.println("========================================");
                    System.out.println("          CONVERSION HISTORY");
                    System.out.println("========================================");

                    if (history.isEmpty()) {

                        System.out.println("No conversions yet.");

                    } else {

                        int number = 1;

                        for (Conversion conversion : history.getConversions()) {

                            System.out.printf(
                                    "%d. %s %s → %s %s | Rate: %s | %s%n",
                                    number,
                                    conversion.getAmount().setScale(2, RoundingMode.HALF_UP),
                                    conversion.getFrom(),
                                    conversion.getResult().setScale(2, RoundingMode.HALF_UP),
                                    conversion.getTo(),
                                    conversion.getExchangeRate().setScale(4, RoundingMode.HALF_UP),
                                    conversion.getTimestamp()
                            );

                            number++;
                        }
                    }
                }

                case 3 -> {

                    running = false;

                    System.out.println(
                            "Thank you for using Currency Converter."
                    );
                }

                default -> System.out.println(
                        "Invalid option. Please choose 1, 2, or 3."
                );
            }
        }

        inputHandler.close();
    }
}