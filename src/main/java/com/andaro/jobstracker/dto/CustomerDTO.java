package com.andaro.jobstracker.dto;

import java.time.Instant;
import java.util.UUID;

public record CustomerDTO(
        UUID id,
        String firstName,
        String lastName,
        String address,
        String city,
        String zipCode,
        Instant createdOn,
        Instant modifiedOn
) {
}
