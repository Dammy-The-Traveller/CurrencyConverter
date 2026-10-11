// Declares the package folder path where this class belongs
package com.damilare.currencyconverter;

// Imports the List interface to return collections of currencies
import java.util.List;
import java.util.Objects;

// Defines a public, immutable final class representing an individual currency
public final class Currency {

    // Defines a public constant static instance for United States Dollar
    public static final Currency USD = new Currency("USD", "US Dollar", "$");
    // Defines a public constant static instance for Ghanaian Cedi
    public static final Currency GHS = new Currency("GHS", "Ghanaian Cedi", "₵");
    // Defines a public constant static instance for Euro
    public static final Currency EUR = new Currency("EUR", "Euro", "€");
    // Defines a public constant static instance for British Pound
    public static final Currency GBP = new Currency("GBP", "British Pound", "£");
    // Defines a public constant static instance for Nigerian Naira
    public static final Currency NGN = new Currency("NGN", "Nigerian Naira", "₦");

    // Declares an immutable field storing the 3-letter ISO code (e.g., USD)
    private final String code;
    // Declares an immutable field storing the descriptive full name of the currency
    private final String name;
    // Declares an immutable field storing the symbol character representation (e.g., $)
    private final String symbol;

    // Constructor taking code, name, and symbol to create a new Currency instance
    public Currency(String code, String name, String symbol) {
        // Validates that the code is not null and strictly consists of three uppercase letters
        if (code == null || !code.matches("[A-Z]{3}")) {
            // Throws an exception if the currency code format is invalid
            throw new IllegalArgumentException("Currency code must contain three uppercase letters.");
        }

        // Assigns and stores the validated currency code
        this.code = code;
        // Ensures the name is not null and assigns it, throwing a NullPointerException if null
        this.name = Objects.requireNonNull(name, "Currency name cannot be null.");
        // Sets symbol to the code itself if the provided symbol is null or blank, otherwise uses the symbol
        this.symbol = symbol == null || symbol.isBlank() ? code : symbol;
    }

    // Public getter method to retrieve the 3-letter currency code
    public String getCode() {
        // Returns the currency code string
        return code;
    }

    // Public getter method to retrieve the full currency name
    public String getName() {
        // Returns the currency name string
        return name;
    }

    // Public getter method to retrieve the currency symbol
    public String getSymbol() {
        // Returns the currency symbol string
        return symbol;
    }

    // Public static method providing a predefined default list of fallback currencies
    public static List<Currency> fallbackCurrencies() {
        // Returns an immutable list containing the standard default currencies
        return List.of(USD, GHS, EUR, GBP, NGN);
    }

    // Overrides toString to return a clean string representation (used by UI components like JComboBox)
    @Override
    public String toString() {
        // Returns just the currency code text
        return code;
    }

    // Overrides equals to determine if two Currency objects represent the same currency
    @Override
    public boolean equals(Object object) {
        // Checks if both variables point to the exact same object instance in memory
        if (this == object) {
            // Returns true because they are identical references
            return true;
        }

        // Checks if the given object is null or not an instance of the Currency class
        if (!(object instanceof Currency currency)) {
            // Returns false since types do not match
            return false;
        }

        // Compares equality based strictly on their unique currency code values
        return code.equals(currency.code);
    }

    // Overrides hashCode to generate a unique numeric hash value based on the currency code
    @Override
    public int hashCode() {
        // Returns the hash code integer generated from the currency code string
        return code.hashCode();
    }
}