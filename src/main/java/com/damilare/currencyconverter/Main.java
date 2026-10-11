// Declares the package folder path where this Main class file belongs
package com.damilare.currencyconverter;

// Imports the List interface to hold collections of currency objects
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

// Defines the public Main entry-point class that starts the application execution
public class Main {

    // Defines the standard Java main method which runs automatically when the program launches
    public static void main(String[] args) {
        // Creates an instance of CurrencyService to fetch available world currencies
        CurrencyService currencyService = new CurrencyService();

        // Declares a list variable to store the supported currencies
        List<Currency> currencies;

        // Tries to fetch the live list of currencies from the network/API service
        try {
            // Calls service method to retrieve live currencies list
            currencies = currencyService.getCurrencies();
            // Prints a success message to the console showing how many currencies were loaded
            System.out.println(
                    "Loaded " + currencies.size() + " supported currencies."
            );
        // Catches any connection or runtime errors encountered while fetching live currencies
        } catch (RuntimeException e) {
            // Falls back to the hardcoded default currency list if the API request fails
            currencies = Currency.fallbackCurrencies();

            // Prints a warning message to the console indicating fallback usage
            System.out.println(
                    "Warning: Could not load currencies from the API. "
                            + "Using the fallback list."
            );
            // Prints the specific error cause to the console for debugging purposes
            System.out.println("Reason: " + e.getMessage());
        }

        // Creates a final or effectively final reference copy of currencies for use inside the lambda thread block
        List<Currency> supportedCurrencies = currencies;

        // Schedules GUI window creation on Java's safe Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            // Tries to instantiate and display the main window frame
            try {
                // Creates a new instance of the GUI window, passing the supported currencies list
                CurrencyConverterGUI gui =
                        new CurrencyConverterGUI(supportedCurrencies);

                // Makes the application window visible on the desktop screen
                gui.setVisible(true);
            // Catches any unexpected errors that occur while launching the window
            } catch (Exception e) {
                // Pops up a critical error dialog window informing the user about the startup failure
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