package com.andaro.jobstracker.dto;

public record CreateContractorDTO(
        String firstName,
        String lastName,
        String companyName,
        TradeTypeDTO tradeType,
        String licenseNumber,
        String zipCode,
        String address,
        String address2,
        String city,
        String state,
        String country,
        String emailAddress,
        String phoneNumber
) { }
