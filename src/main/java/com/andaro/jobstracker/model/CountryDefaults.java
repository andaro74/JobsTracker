package com.andaro.jobstracker.model;

public final class CountryDefaults {
    public static final String DEFAULT_COUNTRY = "US";

    private CountryDefaults() {
    }

    public static String defaultIfBlank(String value) {
        return (value == null || value.isBlank()) ? DEFAULT_COUNTRY : value;
    }
}

