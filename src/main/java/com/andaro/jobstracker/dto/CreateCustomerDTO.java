package com.andaro.jobstracker.dto;

public record CreateCustomerDTO(
        String firstName,
        String lastName,
        String address,
        String city,
        String state,
        String zipCode
) { }
