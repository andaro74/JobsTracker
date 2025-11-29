package com.andaro.jobstracker.service;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorServiceImpl implements IdGeneratorService {

    /**
     * Generates a Monotonic ULID string.
     * Thread-safe and sortable.
     */
    public String generateSortKey(String prefix) {
        return prefix + "_" + UlidCreator.getMonotonicUlid().toString();
    }


    /**
     * Generates a Catalog Monotonic ULID string.
     * Thread-safe and sortable.
     */
    public String createCatalogId() {
        return "cat_" + UlidCreator.getMonotonicUlid().toString();
    }

    /**
     * Generates a Job Monotonic ULID string.
     * Thread-safe and sortable.
     */
    public String createJobId() {
        return "job_" + UlidCreator.getMonotonicUlid().toString();
    }

    /**
     * Generates a Customer Monotonic ULID string.
     * Thread-safe and sortable.
     */
    public String createCustomerId() {
        return "cus_" + UlidCreator.getMonotonicUlid().toString();
    }

    /**
     * Generates a Contractor Monotonic ULID string.
     * Thread-safe and sortable.
     */
    public String createContractorId() {
        return "con_" + UlidCreator.getMonotonicUlid().toString();
    }

}