package com.andaro.jobstracker.service;

import com.andaro.jobstracker.model.JobItem;

public interface JobsEventPublisher {
    public void publishJobsCreatedEvent(String key, JobItem jobItem);
}
