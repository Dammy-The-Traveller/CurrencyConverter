// Package declaration matching the project test directory structure
package com.damilare.currencyconverter;

// Imports the BigDecimal class for precise arithmetic operations
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

// Test class containing unit tests for the CurrencyConverter logic
class CurrencyConverterTest {

    // Instantiates an instance of CurrencyConverter to be tested across test cases
    private final CurrencyConverter converter = new CurrencyConverter();

    // Marks this method as a JUnit 5 unit test
    @Test
    // Test case verifying that valid amounts and rates produce the correct converted total
    void convertsAmountUsingExchangeRate() {
        // Calls the convert method with an amount of 100 and rate of 11.8
        BigDecimal result = converter.convert(
                new BigDecimal("100"),
                new BigDecimal("11.8")
        );

        // Asserts that the calculated result equals the expected BigDecimal value of 1180.0
        assertEquals(new BigDecimal("1180.0"), result);
    }

    // Marks this method as a JUnit 5 unit test
    @Test
    // Test case verifying that converting zero returns zero without errors
    void acceptsZeroAmount() {
        // Calls the convert method with an amount of zero and a valid exchange rate
        BigDecimal result = converter.convert(
                BigDecimal.ZERO,
                new BigDecimal("11.8")
        );

        // Asserts that the result equals zero numerically using BigDecimal compareTo
        assertEquals(0, result.compareTo(BigDecimal.ZERO));
    }

    // Marks this method as a JUnit 5 unit test
    @Test
    // Test case verifying that negative amounts are rejected
    void rejectsNegativeAmount() {
        // Asserts that an IllegalArgumentException is thrown when a negative amount is passed
        assertThrows(
                IllegalArgumentException.class,
                // Lambda expression invoking convert with a negative amount (-10)
                () -> converter.convert(
                        new BigDecimal("-10"),
                        new BigDecimal("11.8")
                )
        );
    }

    // Marks this method as a JUnit 5 unit test
    @Test
    // Test case verifying that an exchange rate of zero is rejected
    void rejectsZeroExchangeRate() {
        // Asserts that an IllegalArgumentException is thrown when rate is zero
        assertThrows(
                IllegalArgumentException.class,
                // Lambda expression invoking convert with an exchange rate of zero
                () -> converter.convert(
                        new BigDecimal("100"),
                        BigDecimal.ZERO
                )
        );
    }

    // Marks this method as a JUnit 5 unit test
    @Test
    // Test case verifying that negative exchange rates are rejected
    void rejectsNegativeExchangeRate() {
        // Asserts that an IllegalArgumentException is thrown when rate is negative
        assertThrows(
                IllegalArgumentException.class,
                // Lambda expression invoking convert with a negative exchange rate (-1)
                () -> converter.convert(
                        new BigDecimal("100"),
                        new BigDecimal("-1")
                )
        );
    }
}