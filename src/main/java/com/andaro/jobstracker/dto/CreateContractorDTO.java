package com.andaro.jobstracker.dto;

public record CreateContractorDTO(
        String firstName,
        String lastName,
        String companyName,
        String specialty,
        String licenseNumber,
        String zipCode
) { }
