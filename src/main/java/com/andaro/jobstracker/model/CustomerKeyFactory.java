package com.andaro.jobstracker.model;

import com.andaro.jobstracker.repository.DynamoKeyBuilder;
import org.springframework.stereotype.Component;

@Component
public class CustomerKeyFactory {

    private static final String PK_PREFIX = "CUSTOMER#";
    private static final String SK_PREFIX = "LOC";

    public String getPartitionKey(String customerId) {
        return PK_PREFIX + customerId;
    }

    public String getSortKey(String state, String city, String zip) {
        return SK_PREFIX + state + "#" + city + "#" + zip;
    }
}

