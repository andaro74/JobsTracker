package com.andaro.jobstracker.model;
import org.springframework.stereotype.Component;

@Component
public class CustomerKeyFactory {

    private static final String PK_PREFIX = "CUSTOMER#";
    private static final String SK_PREFIX = "LOC#";

    public String getPartitionKey(String customerId) {
        return PK_PREFIX + customerId;
    }

    public String getSortKey(String state, String city, String zip) {
        if (state == null || city == null || zip == null) {
            throw new IllegalArgumentException("Address components cannot be null");
        }

        String cleanState = state.trim().toUpperCase();
        String cleanCity = city.trim().toUpperCase();
        String cleanZip = zip.trim(); // Zips are usually numeric, but keep as string

        return SK_PREFIX + cleanState + "#" + cleanCity + "#" + cleanZip;
    }
}

