
package com.damilare.currencyconverter;

import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        CurrencyService currencyService = new CurrencyService();

        List<Currency> currencies;

        try {
            currencies = currencyService.getCurrencies();
            System.out.println(
                    "Loaded " + currencies.size() + " supported currencies."
            );
        } catch (RuntimeException e) {
            currencies = Currency.fallbackCurrencies();

            System.out.println(
                    "Warning: Could not load currencies from the API. "
                            + "Using the fallback list."
            );
            System.out.println("Reason: " + e.getMessage());
        }

        List<Currency> supportedCurrencies = currencies;

        SwingUtilities.invokeLater(() -> {
            try {
                CurrencyConverterGUI gui =
                        new CurrencyConverterGUI(supportedCurrencies);

                gui.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Could not start the application: " + e.getMessage(),
                        "Startup Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}