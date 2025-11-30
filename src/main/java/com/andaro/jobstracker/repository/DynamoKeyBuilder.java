package com.andaro.jobstracker.repository;

import java.util.Objects;

public final class DynamoKeyBuilder {

    private static final String PREFIX = "LOC";
    private static final String DELIMITER = "#";

    private DynamoKeyBuilder() {
    }

    public static String buildPk(String prefix, String id) {
        return buildKey(prefix, id);
    }

    public static String buildSk(String prefix, String id) {
        return buildKey(prefix, id);
    }

    private static String buildKey(String prefix, String id) {
        Objects.requireNonNull(prefix, "prefix must not be null");
        Objects.requireNonNull(id, "id must not be null");
        return prefix + id;
    }




    public static String createAddressSortKey(String state, String city, String zip) {
        if (state == null || city == null || zip == null) {
            throw new IllegalArgumentException("Address components cannot be null");
        }

        // 1. Normalize: Trim whitespace and convert to Upper Case
        String cleanState = state.trim().toUpperCase();
        String cleanCity = city.trim().toUpperCase();
        String cleanZip = zip.trim(); // Zips are usually numeric, but keep as string

        // 2. Validate State (Optional but recommended)
        if (cleanState.length() != 2) {
            // Handle error or conversion from full name to abbreviation
        }

        // 3. Construct: LOC#CA#LOS ANGELES#90001
        return String.join(DELIMITER, PREFIX, cleanState, cleanCity, cleanZip);
    }
}
