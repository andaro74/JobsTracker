package com.andaro.jobstracker.dto;

import java.util.Date;

public record JobItemDTO(
        String catalogId,
        String customerId,
        String contractorId,
        String jobDescription,
        JobStatusDTO jobStatus,
        Date expectedCompletion,
        Date actualCompletion,
        Date createdOn,
        Date modifiedOn
) { }
