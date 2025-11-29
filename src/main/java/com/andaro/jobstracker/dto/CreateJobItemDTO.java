package com.andaro.jobstracker.dto;

import java.time.Instant;

public record CreateJobItemDTO(
        String catalogId,
        String customerId,
        String contractorId,
        String jobDescription,
        JobStatusDTO jobStatus,
        Instant expectedCompletion
        ) { }
