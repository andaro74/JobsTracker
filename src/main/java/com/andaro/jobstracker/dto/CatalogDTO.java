package com.andaro.jobstracker.dto;

import java.time.Instant;
import java.util.UUID;

public record CatalogDTO(
        String catalogId,
        String catalogName,
        String catalogDescription,
        Double price,
        String sku,
        TradeTypeDTO tradeType,
        PriceRateTypeDTO priceRateType,
        Instant createdOn,
        Instant modifiedOn
) { }
