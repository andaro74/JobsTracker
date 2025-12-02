package com.andaro.jobstracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CustomerRequest(
        @NotBlank(message = "firstName is required")
        String firstName,
        @NotBlank(message = "lastName is required")
        String lastName,
        @NotBlank(message = "address is required")
        String address,
        String address2,
        @NotBlank(message = "city is required")
        String city,
        @NotBlank(message = "state is required")
        String state,
        @NotBlank(message = "zipCode is required")
        String zipCode,
        String country,
        @NotBlank(message = "emailAddress is required")
        @Email(message = "emailAddress must be a valid email")
        String emailAddress,
        @NotBlank(message = "phoneNumber is required")
        String phoneNumber,
        String companyName
) { }
