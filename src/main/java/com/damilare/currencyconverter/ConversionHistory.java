package com.damilare.currencyconverter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConversionHistory {

    private final List<Conversion> conversions;

    public ConversionHistory() {
        conversions = new ArrayList<>();
    }

    public void addConversion(Conversion conversion) {
        conversions.add(conversion);
    }

    public List<Conversion> getConversions() {
        return Collections.unmodifiableList(conversions);
    }

    public boolean isEmpty() {
        return conversions.isEmpty();
    }

    public void clear() {
        conversions.clear();
    }
}