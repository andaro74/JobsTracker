package com.andaro.jobstracker.dto;

import java.util.Date;

public record JobItemRequestDTO(
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
