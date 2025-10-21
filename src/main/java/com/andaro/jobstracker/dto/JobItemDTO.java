package com.andaro.jobstracker.dto;

import java.util.Date;
import java.util.UUID;

public record JobItemDTO(
        UUID id,
        UUID catalogId,
        UUID customerId,
        String jobName,
        String jobDescription,
        String assignedTo,
        String customerName,
        String jobStatus,
        Date expectedCompletion,
        Date actualCompletion,
        Date createdOn,
        Date modifiedOn
) { }
