package com.andaro.jobstracker.events;

import com.andaro.jobstracker.model.JobStatus;
import java.time.Instant;

public record JobItemCreateEvent(
        String jobId,
        String jobDescription,
        JobStatus jobStatus,
        Instant createdOn
) {}
