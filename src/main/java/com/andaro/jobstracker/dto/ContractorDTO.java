package com.andaro.jobstracker.dto;

import java.time.Instant;
import java.util.UUID;

public record ContractorDTO(
        UUID id,
        String firstName,
        String lastName,
        String companyName,
        String specialty,
        String licenseNumber,
        String zipCode,
        Instant createdOn,
        Instant modifiedOn
) {
}
