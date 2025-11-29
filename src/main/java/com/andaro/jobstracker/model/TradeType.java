package com.andaro.jobstracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TradeType {
    PLUMBING("Plumbing"),
    HVAC("HVAC"),
    ELECTRICAL("Electrical"),
    ROOFING("Roofing"),
    LANDSCAPING("Landscaping"),
    SWIMMING_POOL("Swimming Pool"),
    PAINTING("Painting"),
    FLOORING("Flooring"),
    GLAZING("Glazing"),
    INSULATION("Insulation"),
    CARPENTRY("Carpentry"),
    PAVING("Paving"),
    MASONRY("Masonry");

    private final String label;

    TradeType(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static TradeType fromString(String value) {
        if (value == null) return null;
        String norm = value.trim();
        // try exact label match (case-insensitive)
        for (TradeType t : values()) {
            if (t.label.equalsIgnoreCase(norm)) {
                return t;
            }
        }
        // try enum name match accepting spaces/underscores
        String nameLike = norm.toUpperCase().replace(' ', '_');
        for (TradeType t : values()) {
            if (t.name().equals(nameLike)) return t;
        }
        throw new IllegalArgumentException("Unknown TradeType: " + value);
    }
}

