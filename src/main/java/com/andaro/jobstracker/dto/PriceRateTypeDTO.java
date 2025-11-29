package com.andaro.jobstracker.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PriceRateTypeDTO {
    HOURLY("Hourly"),
    PER_JOB("Per Job"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly"),
    PER_VISIT("Per Visit"),
    PER_UNIT("Per Unit");

    private final String label;

    PriceRateTypeDTO(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static PriceRateTypeDTO fromString(String value) {
        if (value == null) return null;
        String norm = value.trim();
        for (PriceRateTypeDTO t : values()) {
            if (t.label.equalsIgnoreCase(norm)) return t;
        }
        String nameLike = norm.toUpperCase().replace(' ', '_');
        for (PriceRateTypeDTO t : values()) {
            if (t.name().equals(nameLike)) return t;
        }
        throw new IllegalArgumentException("Unknown PriceRateTypeDTO: " + value);
    }
}

