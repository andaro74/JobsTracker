package com.andaro.jobstracker.dto;

public record CreateCustomerDTO(
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
        String companyName
) { }
