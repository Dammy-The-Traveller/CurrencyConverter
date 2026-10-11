
package com.damilare.currencyconverter;

import java.math.BigDecimal;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConversionHistoryTest {

    @TempDir
    Path tempDir;

    private ConversionHistory history;

    @BeforeEach
    void setUp() {
        history = new ConversionHistory(
                tempDir.resolve("conversion-history.json")
        );
    }

    @Test
    void startsEmpty() {
        assertTrue(history.isEmpty());
        assertEquals(0, history.getConversions().size());
    }

    @Test
    void storesConversion() {
        Conversion conversion = createConversion();

        history.addConversion(conversion);

        assertEquals(1, history.getConversions().size());
        assertFalse(history.isEmpty());
        assertEquals(conversion, history.getConversions().get(0));
    }

    @Test
    void returnsUnmodifiableConversionList() {
        history.addConversion(createConversion());

        assertThrows(
                UnsupportedOperationException.class,
                () -> history.getConversions().clear()
        );
    }

    @Test
    void clearsHistory() {
        history.addConversion(createConversion());

        history.clear();

        assertTrue(history.isEmpty());
        assertEquals(0, history.getConversions().size());
    }

    @Test
    void canStoreMultipleConversions() {
        history.addConversion(createConversion());
        history.addConversion(createConversion());

        assertEquals(2, history.getConversions().size());
    }


@Test
void persistsConversionsAcrossInstances() {
    Conversion conversion = createConversion();
    history.addConversion(conversion);

    ConversionHistory reloadedHistory = new ConversionHistory(
            tempDir.resolve("conversion-history.json")
    );

    assertEquals(1, reloadedHistory.getConversions().size());

    Conversion restored = reloadedHistory.getConversions().get(0);
    assertEquals(conversion.getAmount(), restored.getAmount());
    assertEquals(conversion.getFrom().getCode(), restored.getFrom().getCode());
    assertEquals(conversion.getTo().getCode(), restored.getTo().getCode());
    assertEquals(conversion.getExchangeRate(), restored.getExchangeRate());
    assertEquals(conversion.getResult(), restored.getResult());
}

@Test
void preservesTimestampWhenReloadingHistory() {
    Conversion conversion = createConversion();
    history.addConversion(conversion);

    ConversionHistory reloadedHistory = new ConversionHistory(
            tempDir.resolve("conversion-history.json")
    );

    Conversion restored = reloadedHistory.getConversions().get(0);

    assertEquals(
            conversion.getTimestamp(),
            restored.getTimestamp()
    );
}

@Test
void clearingHistoryRemovesSavedConversions() {
    history.addConversion(createConversion());

    history.clear();

    ConversionHistory reloadedHistory = new ConversionHistory(
            tempDir.resolve("conversion-history.json")
    );

    assertTrue(reloadedHistory.isEmpty());
}

    private Conversion createConversion() {
        return new Conversion(
                new BigDecimal("100"),
                Currency.USD,
                Currency.GHS,
                new BigDecimal("11.7303"),
                new BigDecimal("1173.03")
        );
    }
}