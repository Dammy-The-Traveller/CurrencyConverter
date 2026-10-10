// Package declaration matching the project directory structure
package com.damilare.currencyconverter;

// Imports the class for handling network input/output exceptions
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

// Defines the public class responsible for fetching and caching currency exchange rates from an external API
public class ExchangeRateService {

    // Defines a constant storing the base URL endpoint of the exchange rate API
    private static final String API_URL = "https://api.frankfurter.dev/v2/rate/";
    // Defines a constant setting the cache duration limit to 24 hours
    private static final Duration CACHE_DURATION = Duration.ofHours(24);

    // Creates a thread-safe in-memory cache map storing currency pair strings against CachedRate objects
    private final Map<String, CachedRate> rateCache = new ConcurrentHashMap<>();

    // Compact record class to store an exchange rate along with the exact timestamp it was retrieved
    private record CachedRate(BigDecimal rate, Instant fetchedAt) {

        // Method to check whether the cached entry has exceeded its 24-hour expiration window
        boolean isExpired() {
            // Adds 24 hours to the fetched timestamp and checks if that time is before the current moment
            return fetchedAt.plus(CACHE_DURATION).isBefore(Instant.now());
        }
    }

    // Declares an immutable HTTP client instance for executing network requests
    private final HttpClient httpClient;
    // Declares an immutable Jackson ObjectMapper instance for JSON deserialization
    private final ObjectMapper objectMapper;

    // Constructor to initialize the HTTP client and JSON mapper instances
    public ExchangeRateService() {
        // Initializes the HTTP client using a builder pattern
        httpClient = HttpClient.newBuilder()
                // Sets a 10-second timeout for establishing the initial server connection
                .connectTimeout(Duration.ofSeconds(10))
                // Builds and assigns the configured HttpClient instance
                .build();

        // Instantiates the Jackson ObjectMapper for parsing JSON data
        objectMapper = new ObjectMapper();
    }

    // Method that fetches and returns the conversion rate between two currencies as a BigDecimal
    public BigDecimal getRate(Currency from, Currency to) {

        // Checks if the source currency and target currency are the same
        if (from.equals(to)) {
            // Returns 1 immediately since converting a currency to itself requires no calculation
            return BigDecimal.ONE;
        }

        // Constructs a unique lookup key for the cache combining source and target currency names
        String cacheKey = from.getCode() + "_" + to.getCode();

        // Attempts to retrieve a cached exchange rate entry using the lookup key
        CachedRate cachedRate = rateCache.get(cacheKey);

        // Checks if a cached entry exists and is still fresh (not expired)
        if (cachedRate != null && !cachedRate.isExpired()) {
            // Returns the stored rate directly from cache without hitting the API
            return cachedRate.rate();
        }

        // Constructs the dynamic URL string using the base URL and lowercased currency codes
       String url = API_URL + from.getCode() + "/" + to.getCode();

        // Creates an HTTP GET request targeting the constructed URL
        HttpRequest request = HttpRequest.newBuilder()
                // Converts the URL string into a URI object and attaches it to the request
                .uri(URI.create(url))
                // Sets a 15-second timeout limit for receiving the response
                .timeout(Duration.ofSeconds(15))
                // Specifies that this request uses the HTTP GET method
                .GET()
                // Builds and returns the configured HttpRequest object
                .build();

        // Begins a try block to handle network and interruption exceptions during request execution
        try {
            // Sends the HTTP request synchronously and handles the response body as a String
            HttpResponse<String> response = httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            // Checks if the API returned 404 (Not Found) or 422 (Unprocessable Entity) status codes
            if (response.statusCode() == 404
                    || response.statusCode() == 422) {
                // Throws an exception indicating the requested currency pair is unsupported
                throw new RuntimeException(
                        "The API does not support this currency pair."
                );
            }

            // Checks if the HTTP status code is anything other than 200 OK
            if (response.statusCode() != 200) {
                // Throws an exception detailing the non-OK HTTP status code returned
                throw new RuntimeException(
                        "The exchange rate service returned HTTP "
                                + response.statusCode() + "."
                );
            }

            // Deserializes the JSON response body into an ExchangeRateResponse Java object
            ExchangeRateResponse rateResponse =
                    objectMapper.readValue(
                            response.body(),
                            ExchangeRateResponse.class
                    );

            // Extracts the BigDecimal exchange rate value from the parsed response object
            BigDecimal rate = rateResponse.getRate();

            // Validates that the returned rate exists and is strictly greater than zero
            if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
                // Throws an exception if the rate is missing, zero, or negative
                throw new RuntimeException(
                        "The API returned an invalid exchange rate."
                );
            }

            // Saves the freshly retrieved rate and current timestamp into the in-memory cache
            rateCache.put(
                cacheKey,
                new CachedRate(rate, Instant.now())
            );

            // Returns the newly fetched exchange rate to the caller
            return rate;

        // Catches network I/O issues such as lack of internet connection or unreachable hosts
        } catch (IOException e) {
            // Re-throws as a unchecked RuntimeException with a helpful user message and the root cause
            throw new RuntimeException(
                    "Unable to connect to the exchange rate service. "
                            + "Check your internet connection.",
                    e
            );

        // Catches cases where the thread executing the request is interrupted while waiting
        } catch (InterruptedException e) {
            // Re-asserts the thread's interrupted status so higher-level callers are aware
            Thread.currentThread().interrupt();

            // Re-throws as an unchecked RuntimeException indicating execution was cancelled
            throw new RuntimeException(
                    "The exchange rate request was interrupted.",
                    e
            );
        }
    }
}