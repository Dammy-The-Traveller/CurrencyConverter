
package com.damilare.currencyconverter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConversionHistory {

    private final List<Conversion> conversions;
    private final ObjectMapper objectMapper;
    private final Path historyFile;

    public ConversionHistory() {
        this(Path.of("conversion-history.json"));
    }

    public ConversionHistory(Path historyFile) {
        this.conversions = new ArrayList<>();
        this.objectMapper = new ObjectMapper();
        this.historyFile = historyFile;

        loadHistory();
    }

    public void addConversion(Conversion conversion) {
        conversions.add(conversion);

        try {
            saveHistory();
        } catch (RuntimeException e) {
            conversions.remove(conversions.size() - 1);
            throw e;
        }
    }

    public List<Conversion> getConversions() {
        return Collections.unmodifiableList(conversions);
    }

    public boolean isEmpty() {
        return conversions.isEmpty();
    }

    public void clear() {
        List<Conversion> previousConversions = new ArrayList<>(conversions);
        conversions.clear();

        try {
            saveHistory();
        } catch (RuntimeException e) {
            conversions.addAll(previousConversions);
            throw e;
        }
    }

    private void loadHistory() {
        if (Files.notExists(historyFile)) {
            return;
        }

        try {
            JsonNode root = objectMapper.readTree(historyFile.toFile());

            if (root == null || !root.isArray()) {
                throw new IOException("The history file must contain a JSON array.");
            }

            List<Conversion> loadedConversions = new ArrayList<>();

            for (JsonNode node : root) {
                loadedConversions.add(readConversion(node));
            }

            conversions.addAll(loadedConversions);

        } catch (IOException | RuntimeException e) {
            conversions.clear();
            System.err.println(
                    "Warning: Could not load conversion history: " + e.getMessage()
            );
        }
    }

    private Conversion readConversion(JsonNode node) throws IOException {
        String fromCode = requiredText(node, "fromCode");
        String fromName = requiredText(node, "fromName");
        String fromSymbol = requiredText(node, "fromSymbol");

        String toCode = requiredText(node, "toCode");
        String toName = requiredText(node, "toName");
        String toSymbol = requiredText(node, "toSymbol");

        BigDecimal amount = requiredDecimal(node, "amount");
        BigDecimal rate = requiredDecimal(node, "exchangeRate");
        BigDecimal result = requiredDecimal(node, "result");

        String timestampText = requiredText(node, "timestamp");
        LocalDateTime timestamp;

        try {
            timestamp = LocalDateTime.parse(timestampText);
        } catch (RuntimeException e) {
            throw new IOException("Invalid conversion timestamp.", e);
        }

        Currency from = new Currency(fromCode, fromName, fromSymbol);
        Currency to = new Currency(toCode, toName, toSymbol);

        return new Conversion(amount, from, to, rate, result, timestamp);
    }

    private String requiredText(JsonNode node, String field) throws IOException {
        JsonNode value = node.get(field);

        if (value == null || !value.isTextual() || value.asText().isBlank()) {
            throw new IOException("Missing or invalid field: " + field);
        }

        return value.asText();
    }

    private BigDecimal requiredDecimal(JsonNode node, String field)
            throws IOException {
        JsonNode value = node.get(field);

        if (value == null || !value.isNumber()) {
            throw new IOException("Missing or invalid numeric field: " + field);
        }

        return value.decimalValue();
    }

    private void saveHistory() {
        ArrayNode root = objectMapper.createArrayNode();

        for (Conversion conversion : conversions) {
            ObjectNode node = root.addObject();

            node.put("amount", conversion.getAmount());
            node.put("fromCode", conversion.getFrom().getCode());
            node.put("fromName", conversion.getFrom().getName());
            node.put("fromSymbol", conversion.getFrom().getSymbol());
            node.put("toCode", conversion.getTo().getCode());
            node.put("toName", conversion.getTo().getName());
            node.put("toSymbol", conversion.getTo().getSymbol());
            node.put("exchangeRate", conversion.getExchangeRate());
            node.put("result", conversion.getResult());
            node.put("timestamp", conversion.getTimestamp().toString());
        }

        try {
            Path parent = historyFile.toAbsolutePath().getParent();

            if (parent != null) {
                Files.createDirectories(parent);
            }

            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(historyFile.toFile(), root);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not save conversion history: " + e.getMessage(), e
            );
        }
    }
}