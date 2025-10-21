package com.andaro.jobstracker.service;

import com.andaro.jobstracker.events.JobItemCreateEvent;

public interface JobItemEventOrchestrator {
    void processNewJobItem(JobItemCreateEvent event);
}
