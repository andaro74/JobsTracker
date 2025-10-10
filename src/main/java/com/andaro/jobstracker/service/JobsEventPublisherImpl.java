package com.andaro.jobstracker.service;

import com.andaro.jobstracker.model.JobItem;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobsEventPublisherImpl implements JobsEventPublisher {

    private final KafkaTemplate<String, JobItemCreateEvent> kafkaTemplate;
    private static final String JOBS_TOPIC = "jobs-created-event";

    public JobsEventPublisherImpl(KafkaTemplate<String, JobItemCreateEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public record JobItemCreateEvent(
        String jobName,
        String jobDescription)
        {};

    public void publishJobsCreatedEvent(String key, JobItem jobItem){
        try {
            JobItemCreateEvent itemCreateEvent=new JobItemCreateEvent(jobItem.getJobName(),jobItem.getJobDescription());
            kafkaTemplate.send(JOBS_TOPIC, key, itemCreateEvent);
            System.out.println("Successfully published the Job");
        } catch (Exception ex){
            System.err.println("Failed to publish Jobs event publisher.");
        }
    }

}
