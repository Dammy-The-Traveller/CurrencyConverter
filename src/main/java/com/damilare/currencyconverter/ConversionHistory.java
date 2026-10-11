// Declares the package folder path where this file belongs
package com.damilare.currencyconverter;

// Imports Jackson class to read and navigate JSON tree elements
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

// Defines the public class that manages saving, loading, and tracking past conversion records
public class ConversionHistory {

    // Declares an in-memory list holding all recorded conversion items
    private final List<Conversion> conversions;
    // Declares the Jackson ObjectMapper tool used for reading and writing JSON data files
    private final ObjectMapper objectMapper;
    // Declares the file path where history data is saved on the hard drive
    private final Path historyFile;

    // Default constructor that delegates to the parameterized constructor using a default file name
  
public ConversionHistory() {
    this(getDefaultHistoryPath());
}

private static Path getDefaultHistoryPath() {
    String appData = System.getenv("APPDATA");

    Path appDataDirectory;

    if (appData != null && !appData.isBlank()) {
        appDataDirectory = Path.of(appData);
    } else {
        appDataDirectory = Path.of(System.getProperty("user.home"));
    }

    return appDataDirectory
            .resolve("CurrencyConverter")
            .resolve("conversion-history.json");
}


    // Parameterized constructor allowing a custom file path for storing history records
    public ConversionHistory(Path historyFile) {
        // Initializes the memory list as an empty ArrayList
        this.conversions = new ArrayList<>();
        // Instantiates the Jackson ObjectMapper parser
        this.objectMapper = new ObjectMapper();
        // Stores the designated file path for history saving/loading
        this.historyFile = historyFile;

        // Automatically attempts to load any previously saved history from disk upon startup
        loadHistory();
    }

    // Public method to add a new conversion record into history and save to disk
    public void addConversion(Conversion conversion) {
        // Adds the new conversion object into the active in-memory list
        conversions.add(conversion);

        // Tries to write the updated history list to the JSON file on disk
        try {
            // Saves current history list to file
            saveHistory();
        // Catches any disk writing errors
        } catch (RuntimeException e) {
            // Removes the newly added conversion from memory to keep state consistent if saving fails
            conversions.remove(conversions.size() - 1);
            // Re-throws the exception upward
            throw e;
        }
    }

    // Public method that returns a safe, unmodifiable view of the conversion history list
    public List<Conversion> getConversions() {
        // Returns a read-only wrapper around the conversions list so outside code cannot modify it directly
        return Collections.unmodifiableList(conversions);
    }

    // Public method checking whether the history list contains any records
    public boolean isEmpty() {
        // Returns true if history list has zero records, otherwise false
        return conversions.isEmpty();
    }

    // Public method to clear all conversion history records from memory and disk
    public void clear() {
        // Creates a backup copy of existing conversions in case saving fails
        List<Conversion> previousConversions = new ArrayList<>(conversions);
        // Clears all entries from the active in-memory list
        conversions.clear();

        // Tries to update the persistent storage file to reflect the cleared history
        try {
            // Saves the empty history state to disk
            saveHistory();
        // Catches disk errors during clearing
        } catch (RuntimeException e) {
            // Restores previous conversions back into memory if disk write fails
            conversions.addAll(previousConversions);
            // Re-throws the error
            throw e;
        }
    }

    // Private helper method to load and parse conversion records from the JSON file on disk
    private void loadHistory() {
        // Checks if the history data file does not exist yet on the computer
        if (Files.notExists(historyFile)) {
            // Exits method early since there is no file to load from yet
            return;
        }

        // Tries to read and parse the JSON file contents
        try {
            // Parses the JSON file into a tree root node using Jackson
            JsonNode root = objectMapper.readTree(historyFile.toFile());

            // Validates that the root node exists and is formatted as a JSON array
            if (root == null || !root.isArray()) {
                // Throws an exception if the file format is incorrect
                throw new IOException("The history file must contain a JSON array.");
            }

            // Initializes a temporary list to hold loaded conversion items
            List<Conversion> loadedConversions = new ArrayList<>();

            // Loops through every individual JSON node element inside the array
            for (JsonNode node : root) {
                // Parses each node into a Conversion object and adds it to the temporary list
                loadedConversions.add(readConversion(node));
            }

            // Adds all successfully loaded conversions into the main history list
            conversions.addAll(loadedConversions);

        // Catches file reading or parsing errors
        } catch (IOException | RuntimeException e) {
            // Clears any partial items from memory
            conversions.clear();
            // Prints a warning message to the console error stream
            System.err.println(
                    "Warning: Could not load conversion history: " + e.getMessage()
            );
        }
    }

    // Private helper method to convert a single JSON node object into a Conversion instance
    private Conversion readConversion(JsonNode node) throws IOException {
        // Extracts required source currency code text string
        String fromCode = requiredText(node, "fromCode");
        // Extracts required source currency name text string
        String fromName = requiredText(node, "fromName");
        // Extracts required source currency symbol text string
        String fromSymbol = requiredText(node, "fromSymbol");

        // Extracts required target currency code text string
        String toCode = requiredText(node, "toCode");
        // Extracts required target currency name text string
        String toName = requiredText(node, "toName");
        // Extracts required target currency symbol text string
        String toSymbol = requiredText(node, "toSymbol");

        // Extracts required original money amount decimal value
        BigDecimal amount = requiredDecimal(node, "amount");
        // Extracts required exchange rate decimal value
        BigDecimal rate = requiredDecimal(node, "exchangeRate");
        // Extracts required calculated result decimal value
        BigDecimal result = requiredDecimal(node, "result");

        // Extracts required timestamp text string
        String timestampText = requiredText(node, "timestamp");
        // Declares variable to hold parsed date-time object
        LocalDateTime timestamp;

        // Tries to parse the timestamp string back into a LocalDateTime object
        try {
            // Parses ISO date-time string
            timestamp = LocalDateTime.parse(timestampText);
        // Catches date formatting errors
        } catch (RuntimeException e) {
            // Throws an IOException if timestamp string is corrupt
            throw new IOException("Invalid conversion timestamp.", e);
        }

        // Re-creates the source Currency object from stored text fields
        Currency from = new Currency(fromCode, fromName, fromSymbol);
        // Re-creates the target Currency object from stored text fields
        Currency to = new Currency(toCode, toName, toSymbol);

        // Returns a fully reconstructed Conversion transaction object including its timestamp
        return new Conversion(amount, from, to, rate, result, timestamp);
    }

    // Private helper method ensuring a text property exists and is valid inside a JSON node
    private String requiredText(JsonNode node, String field) throws IOException {
        // Looks up the specific field node by name
        JsonNode value = node.get(field);

        // Checks if the field is missing, not text, or blank whitespace
        if (value == null || !value.isTextual() || value.asText().isBlank()) {
            // Throws an IOException if field validation fails
            throw new IOException("Missing or invalid field: " + field);
        }

        // Returns the valid text string value
        return value.asText();
    }

    // Private helper method ensuring a numeric decimal property exists and is valid inside a JSON node
    private BigDecimal requiredDecimal(JsonNode node, String field)
            throws IOException {
        // Looks up the specific numeric field node by name
        JsonNode value = node.get(field);

        // Checks if field is missing or not a valid number
        if (value == null || !value.isNumber()) {
            // Throws an IOException if numeric validation fails
            throw new IOException("Missing or invalid numeric field: " + field);
        }

        // Returns the decimal value
        return value.decimalValue();
    }

    // Private helper method to serialize and write all conversion records to disk in JSON format
    private void saveHistory() {
        // Creates a new empty Jackson JSON array container node
        ArrayNode root = objectMapper.createArrayNode();

        // Loops through every conversion record stored in memory
        for (Conversion conversion : conversions) {
            // Creates a new child JSON object node inside the array for the current conversion
            ObjectNode node = root.addObject();

            // Puts the original amount value into JSON object
            node.put("amount", conversion.getAmount());
            // Puts the source currency code into JSON object
            node.put("fromCode", conversion.getFrom().getCode());
            // Puts the source currency name into JSON object
            node.put("fromName", conversion.getFrom().getName());
            // Puts the source currency symbol into JSON object
            node.put("fromSymbol", conversion.getFrom().getSymbol());
            // Puts the target currency code into JSON object
            node.put("toCode", conversion.getTo().getCode());
            // Puts the target currency name into JSON object
            node.put("toName", conversion.getTo().getName());
            // Puts the target currency symbol into JSON object
            node.put("toSymbol", conversion.getTo().getSymbol());
            // Puts the exchange rate value into JSON object
            node.put("exchangeRate", conversion.getExchangeRate());
            // Puts the calculation result value into JSON object
            node.put("result", conversion.getResult());
            // Puts the formatted timestamp string into JSON object
            node.put("timestamp", conversion.getTimestamp().toString());
        }

        // Tries to write the JSON data structure out to the physical file on disk
        try {
            // Gets the parent directory path of the history file location
            Path parent = historyFile.toAbsolutePath().getParent();

            // Checks if parent folder directory path exists
            if (parent != null) {
                // Creates parent directories automatically if they don't exist yet
                Files.createDirectories(parent);
            }

            // Uses Jackson pretty printer to write formatted, indented JSON to the target file
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(historyFile.toFile(), root);

        // Catches disk write or permission errors
        } catch (IOException e) {
            // Wraps and throws as an IllegalStateException if saving fails
            throw new IllegalStateException(
                    "Could not save conversion history: " + e.getMessage(), e
            );
        }
    }
}