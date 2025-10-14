package com.andaro.jobstracker.dto;

import java.time.Instant;
import java.util.UUID;

public record ContractorDTO(
        UUID id,
        String FirstName,
        String LastName,
        String Specialty,
        String LicenseNumber,
        String ZipCode,
        Instant CreatedOn,
        Instant ModifiedOn
) {
}
