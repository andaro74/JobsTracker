package com.andaro.jobstracker.dto;

import java.time.Instant;

public record CustomerDTO(
        String customerId,
        String firstName,
        String lastName,
        String address,
        String address2,
        String city,
        String state,
        String zipCode,
        String country,
        String emailAddress,
        String phoneNumber,
        String companyName,
        Instant createdOn,
        Instant modifiedOn
) {
}
