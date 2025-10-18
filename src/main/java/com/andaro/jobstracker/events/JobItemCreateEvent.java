package com.andaro.jobstracker.events;

import java.time.Instant;

public record JobItemCreateEvent(
        String jobId,
        String jobName,
        String jobDescription,
        String customerName,
        String jobStatus,
        Instant createdOn
) {};