package com.andaro.jobstracker.dto;

import java.time.Instant;
import java.util.Date;

public record CreateJobItemDTO(
        String jobName,
        String jobDescription,
        //String assignedTo,
        String customerName,
        //String jobStatus,
        Instant expectedCompletion //,
        //Date actualCompletion,
        //Date createdOn,
        //Date modifiedOn
        ) { }
