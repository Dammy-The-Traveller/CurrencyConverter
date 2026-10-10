package com.damilare.currencyconverter;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class ConversionHistoryTest {

    @Test
    void startsEmpty() {
        ConversionHistory history = new ConversionHistory();

        assertTrue(history.isEmpty());
        assertEquals(0, history.getConversions().size());
    }

    @Test
    void storesConversion() {
        ConversionHistory history = new ConversionHistory();

        Conversion conversion = new Conversion(
                new BigDecimal("100"),
                Currency.USD,
                Currency.GHS,
                new BigDecimal("11.8"),
                new BigDecimal("1180")
        );

        history.addConversion(conversion);

        assertFalse(history.isEmpty());
        assertEquals(1, history.getConversions().size());
        assertSame(conversion, history.getConversions().get(0));
    }

    @Test
    void canStoreMultipleConversions() {
        ConversionHistory history = new ConversionHistory();

        Conversion first = new Conversion(
                new BigDecimal("100"),
                Currency.USD,
                Currency.GHS,
                new BigDecimal("11.8"),
                new BigDecimal("1180")
        );

        Conversion second = new Conversion(
                new BigDecimal("50"),
                Currency.EUR,
                Currency.USD,
                new BigDecimal("1.1"),
                new BigDecimal("55")
        );

        history.addConversion(first);
        history.addConversion(second);

        assertEquals(2, history.getConversions().size());
    }

    @Test
    void clearsHistory() {
        ConversionHistory history = new ConversionHistory();

        history.addConversion(new Conversion(
                new BigDecimal("100"),
                Currency.USD,
                Currency.GHS,
                new BigDecimal("11.8"),
                new BigDecimal("1180")
        ));

        history.clear();

        assertTrue(history.isEmpty());
    }

    @Test
    void returnedHistoryCannotBeModifiedDirectly() {
        ConversionHistory history = new ConversionHistory();

        List<Conversion> conversions = history.getConversions();

        assertThrows(
                UnsupportedOperationException.class,
                () -> conversions.add(new Conversion(
                        BigDecimal.ONE,
                        Currency.USD,
                        Currency.GHS,
                        new BigDecimal("11.8"),
                        new BigDecimal("11.8")
                ))
        );
    }
}
