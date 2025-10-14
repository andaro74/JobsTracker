package com.andaro.jobstracker.dto;

public record CreateContractorDTO(
        String FirstName,
        String LastName,
        String Specialty,
        String LicenseNumber,
        String ZipCode
) { }
