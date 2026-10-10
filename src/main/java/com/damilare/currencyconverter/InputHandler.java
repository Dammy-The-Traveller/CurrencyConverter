// Package declaration matching the project directory structure
package com.damilare.currencyconverter;

// Imports the BigDecimal class for handling currency amount inputs
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

// Defines the InputHandler class responsible for reading and validating console user inputs
public class InputHandler {

    // Declares an immutable Scanner instance to read system input
    private final Scanner scanner;
    // Declares an immutable List storing the supported currency options
    private final List<Currency> currencies;

    // Default constructor that delegates initialization using fallback currencies
    public InputHandler() {
        // Calls the parameterized constructor passing the default list of fallback currencies
        this(Currency.fallbackCurrencies());
    }

    // Parameterized constructor accepting a custom list of supported currencies
    public InputHandler(List<Currency> currencies) {
        // Validates that the provided currency list is neither null nor empty
        if (currencies == null || currencies.isEmpty()) {
            // Throws an exception if no currencies are available for selection
            throw new IllegalArgumentException(
                    "At least one currency must be provided."
            );
        }

        // Instantiates the scanner instance connected to standard input stream (keyboard)
        this.scanner = new Scanner(System.in);
        // Creates an unmodifiable copy of the provided currency list to ensure immutability
        this.currencies = List.copyOf(currencies);
    }

    // Method to prompt the user and retrieve a valid Currency selection from the list
    public Currency getCurrency(String message) {
        // Starts an infinite loop to repeatedly prompt until a valid selection is made
        while (true) {
            // Prints an empty line for visual spacing in the console
            System.out.println();
            // Prints the custom prompt message instructing the user
            System.out.println(message);

            // Loops through the currency list to print each option with a 1-based index
            for (int i = 0; i < currencies.size(); i++) {
                // Retrieves the currency object at the current index
                Currency currency = currencies.get(i);

                // Formats and displays the option number, currency code, full name, and symbol
                System.out.printf(
                        "%d. %s - %s (%s)%n",
                        i + 1,
                        currency.getCode(),
                        currency.getName(),
                        currency.getSymbol()
                );
            }

            // Prompts the user to enter their choice number
            System.out.print("Choose currency: ");
            // Reads the entire line of user input and trims leading/trailing whitespace
            String input = scanner.nextLine().trim();

            // Attempts to parse and validate the numerical choice
            try {
                // Converts the string input to an integer
                int choice = Integer.parseInt(input);

                // Checks if the choice falls within the valid range of 1 to total currency count
                if (choice >= 1 && choice <= currencies.size()) {
                    // Returns the selected Currency object (adjusting 1-based choice to 0-based list index)
                    return currencies.get(choice - 1);
                }

                // Displays an error message if the integer is out of range
                System.out.printf(
                        "Invalid choice. Please select a number from 1 to %d.%n",
                        currencies.size()
                );

            // Catches invalid inputs that cannot be parsed as integers
            } catch (NumberFormatException e) {
                // Displays an error message prompting for a numeric entry
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Method to prompt the user and retrieve a valid, positive BigDecimal amount
    public BigDecimal getAmount() {
        // Starts an infinite loop to keep asking until a valid positive amount is entered
        while (true) {
            // Displays prompt asking the user for an amount
            System.out.print("Enter amount: ");
            // Reads input line and removes whitespace
            String input = scanner.nextLine().trim();

            // Attempts to parse the string input as a BigDecimal
            try {
                // Creates a new BigDecimal from the input string
                BigDecimal amount = new BigDecimal(input);

                // Checks if the entered amount is zero or negative
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    // Prints error message requiring positive numbers
                    System.out.println("Amount must be greater than zero.");
                    // Restarts the loop to prompt again
                    continue;
                }

                // Returns the validated positive BigDecimal amount
                return amount;

            // Catches invalid input strings that cannot be converted to BigDecimal
            } catch (NumberFormatException e) {
                // Prints error message asking for a valid numeric input
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Method to prompt and retrieve an integer menu choice
    public int getMenuChoice() {
        // Starts an infinite loop until a valid integer is provided
        while (true) {
            // Displays prompt asking for an option selection
            System.out.print("Choose an option: ");
            // Reads the input string and trims whitespace
            String input = scanner.nextLine().trim();

            // Attempts to parse the input string into an integer
            try {
                // Parses and returns the integer choice
                return Integer.parseInt(input);
            // Catches non-integer inputs
            } catch (NumberFormatException e) {
                // Prints error message asking for a numeric input
                System.out.println("Please enter a valid number.");
            }
        }
    }

    // Method to close the underlying Scanner resource
    public void close() {
        // Closes the scanner to release underlying system resources
        scanner.close();
    }
}