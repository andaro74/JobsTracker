package com.andaro.jobstracker.events;

import java.time.Instant;

public record ContractorCreateEvent(
        String contractorId,
        String firstName,
        String lastName,
        String email,
        String companyName,
        String licenseNumber,
        Instant createdOn
) {
}
