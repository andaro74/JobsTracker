package com.andaro.jobstracker.service;

public interface IdGeneratorService {
    String generateSortKey(String prefix);
    String createCatalogId();
    String createJobId();
    String createCustomerId();
    String createContractorId();
}
