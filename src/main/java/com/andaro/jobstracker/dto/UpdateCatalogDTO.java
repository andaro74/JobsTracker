package com.andaro.jobstracker.dto;

public record UpdateCatalogDTO(
        String catalogDescription,
        Double price,
        TradeTypeDTO tradeType,
        PriceRateTypeDTO priceRateType
) { }

