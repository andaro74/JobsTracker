package com.andaro.jobstracker.model;

import org.springframework.stereotype.Component;

@Component
public class ContractorKeyFactory {

    private static final String PK_PREFIX = "CONTRACTOR";
    private static final String SK_PREFIX = "TRADE";

    public String getPartitionKey(String contractorId) {
        return PK_PREFIX + "#" + contractorId;
    }

    public String getSortKey(String trade, String state, String city, String zip) {
        return SK_PREFIX + "#" + trade + "#LOC#" + state + "#" + city + "#" + zip;
    }
}

