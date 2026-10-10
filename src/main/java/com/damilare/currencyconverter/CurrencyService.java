// Package declaration matching the project directory structure
package com.damilare.currencyconverter;

// Imports the class for handling network input/output exceptions
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

// Defines the public class responsible for fetching supported currencies from an external API
public class CurrencyService {

    // Defines a constant holding the URL endpoint for supported currencies
    private static final String CURRENCIES_URL =
            "https://api.frankfurter.dev/v2/currencies";

    // Declares an immutable HTTP client instance for executing network requests
    private final HttpClient httpClient;
    // Declares an immutable Jackson ObjectMapper instance for JSON parsing
    private final ObjectMapper objectMapper;

    // Constructor initializing HTTP client settings and ObjectMapper instance
    public CurrencyService() {
        // Initializes the HTTP client using a builder pattern
        this.httpClient = HttpClient.newBuilder()
                // Sets a 10-second timeout for establishing the initial connection
                .connectTimeout(Duration.ofSeconds(10))
                // Builds and assigns the configured HttpClient instance
                .build();

        // Instantiates the Jackson ObjectMapper for tree parsing
        this.objectMapper = new ObjectMapper();
    }

    // Method to fetch, parse, validate, and return a sorted list of supported currencies
    public List<Currency> getCurrencies() {
        // Creates an HTTP GET request targeting the currencies endpoint
        HttpRequest request = HttpRequest.newBuilder()
                // Converts the endpoint string into a URI object and attaches it
                .uri(URI.create(CURRENCIES_URL))
                // Sets a 15-second response timeout limit
                .timeout(Duration.ofSeconds(15))
                // Specifies the HTTP GET method
                .GET()
                // Builds the HttpRequest object
                .build();

        // Begins a try block to handle network and parsing exceptions
        try {
            // Sends the request synchronously and handles the response body as a String
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            // Checks if the response status code is anything other than 200 OK
            if (response.statusCode() != 200) {
                // Throws an IOException with the status code
                throw new IOException(
                        "Currency API returned HTTP " + response.statusCode()
                );
            }

            // Parses the raw response JSON string into a Jackson JsonNode tree structure
            JsonNode currenciesJson = objectMapper.readTree(response.body());

            // Validates that the returned JSON node is a non-empty array
            if (!currenciesJson.isArray() || currenciesJson.isEmpty()) {
                // Throws an IOException if the JSON structure is invalid or empty
                throw new IOException("Currency API returned an invalid currency list.");
            }

            // Initializes a mutable List to hold successfully parsed Currency objects
            List<Currency> currencies = new ArrayList<>();

            // Iterates over each item node in the JSON array
            for (JsonNode item : currenciesJson) {
                // Safely retrieves the ISO code string or defaults to empty string if missing
                String code = item.path("iso_code").asText("");
                // Safely retrieves the currency name string or defaults to empty string if missing
                String name = item.path("name").asText("");
                // Safely retrieves the currency symbol string or defaults to empty string if missing
                String symbol = item.path("symbol").asText("");

                // Validates that code consists of exactly 3 uppercase letters and name is non-blank
                if (code.matches("[A-Z]{3}") && !name.isBlank()) {
                    // Instantiates a new Currency object and adds it to the list
                    currencies.add(new Currency(code, name, symbol));
                }
            }

            // Checks if no valid currencies survived the validation filter
            if (currencies.isEmpty()) {
                // Throws an IOException indicating missing valid currency data
                throw new IOException("No valid currencies were returned.");
            }

            // Sorts the currency list alphabetically by currency ISO code
            currencies.sort(Comparator.comparing(Currency::getCode));
            // Returns an unmodifiable copy of the sorted currency list
            return List.copyOf(currencies);

        // Catches network or JSON parsing exceptions
        } catch (IOException e) {
            // Re-throws as a RuntimeException detailing the failure reason
            throw new RuntimeException(
                    "Unable to load supported currencies: " + e.getMessage(),
                    e
            );
        // Catches thread interruption exceptions during request execution
        } catch (InterruptedException e) {
            // Re-asserts the interrupted status on the current thread
            Thread.currentThread().interrupt();
            // Re-throws as a RuntimeException indicating execution was cancelled
            throw new RuntimeException("Currency loading was interrupted.", e);
        }
    }
}