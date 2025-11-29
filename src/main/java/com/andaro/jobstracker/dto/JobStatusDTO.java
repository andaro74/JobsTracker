package com.andaro.jobstracker.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum JobStatusDTO {
    REQUESTED("Requested"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    DELAYED("Delayed"),
    CANCELLED("Cancelled");

    private final String label;

    JobStatusDTO(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public static JobStatusDTO fromString(String value) {
        if (value == null) return null;
        String norm = value.trim();
        for (JobStatusDTO s : values()) {
            if (s.label.equalsIgnoreCase(norm)) return s;
        }
        String nameLike = norm.toUpperCase().replace(' ', '_');
        for (JobStatusDTO s : values()) {
            if (s.name().equals(nameLike)) return s;
        }
        throw new IllegalArgumentException("Unknown JobStatusDTO: " + value);
    }
}

