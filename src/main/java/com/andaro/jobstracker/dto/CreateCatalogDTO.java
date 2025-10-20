package com.andaro.jobstracker.dto;

public record CreateCatalogDTO(
        String catalogName,
        String catalogDescription,
        Double hourlyRate
        ) { }
