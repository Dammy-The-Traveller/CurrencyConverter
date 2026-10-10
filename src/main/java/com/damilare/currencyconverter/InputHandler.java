package com.damilare.currencyconverter; 
import java.math.BigDecimal;
import java.util.Scanner; 
public class InputHandler {

    private final Scanner scanner;

    public InputHandler() { // Constructor for initializing the InputHandler class.
        scanner = new Scanner(System.in); // Instantiates the Scanner to read input from standard system input (keyboard).
    } 

  public Currency getCurrency(String message) {

    Currency[] currencies = Currency.values();

    while (true) {

        System.out.println();
        System.out.println(message);

        for (int i = 0; i < currencies.length; i++) {
            Currency currency = currencies[i];

            System.out.printf(
                    "%d. %s - %s (%s)%n",
                    i + 1,
                    currency.name(),
                    currency.getName(),
                    currency.getSymbol()
            );
        }

        System.out.print("Choose currency: ");

        String input = scanner.nextLine().trim();

        try {

            int choice = Integer.parseInt(input);

            if (choice >= 1 && choice <= currencies.length) {
                return currencies[choice - 1];
            }

            System.out.println(
                    "Invalid choice. Please select a number from 1 to "
                            + currencies.length + "."
            );

        } catch (NumberFormatException e) {

            System.out.println(
                    "Please enter a valid number."
            );
        }
    }
}

    public BigDecimal getAmount() { // Public method that prompts for and returns a valid positive BigDecimal amount.
        while (true) { // Starts an infinite loop to continuously prompt until valid numeric input is entered.
            System.out.print("Enter amount: "); 

            String input = scanner.nextLine().trim(); 

            try {
                BigDecimal amount = new BigDecimal(input); // Converts the string input into a BigDecimal value.

                if (amount.compareTo(BigDecimal.ZERO) <= 0) { 
                    System.out.println("Amount must be greater than zero."); 
                    continue; 
                } 

                return amount; 

            } catch (NumberFormatException e) { 
                System.out.println("Please enter a valid number."); 
            } 
        } 
    }

    public int getMenuChoice() { // Public method that prompts for and returns an integer menu selection.
        while (true) { 
            System.out.print("Choose an option: "); 

            String input = scanner.nextLine().trim(); // Reads user input and strips leading and trailing whitespace.

            try { 
                return Integer.parseInt(input); // Converts the string input to an integer and returns it immediately.

            } catch (NumberFormatException e) { // Catches exception thrown if the string is not a valid integer.
                System.out.println("Please enter a valid number."); 
            } 
        }
    } 

    public void close() { // Public method to release system resources held by the scanner.
        scanner.close(); // Closes the underlying Scanner object.
    } 
} 