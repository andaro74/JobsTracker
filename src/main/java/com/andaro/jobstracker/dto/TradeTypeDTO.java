package com.andaro.jobstracker.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TradeTypeDTO {
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

    TradeTypeDTO(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static TradeTypeDTO fromString(String value) {
        if (value == null) return null;
        String norm = value.trim();
        for (TradeTypeDTO t : values()) {
            if (t.label.equalsIgnoreCase(norm)) {
                return t;
            }
        }
        String nameLike = norm.toUpperCase().replace(' ', '_');
        for (TradeTypeDTO t : values()) {
            if (t.name().equals(nameLike)) return t;
        }
        throw new IllegalArgumentException("Unknown TradeTypeDTO: " + value);
    }
}

