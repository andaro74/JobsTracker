package com.andaro.jobstracker.dto;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

public record CatalogDTO(
        UUID id,
        String catalogName,
        String catalogDescription,
        Double hourlyRate,
        Instant createdOn,
        Instant modifiedOn
) { }
