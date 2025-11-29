package com.andaro.jobstracker.repository;

import java.util.Objects;

public final class DynamoKeyBuilder {
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
}
