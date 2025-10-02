package com.andaro.jobstracker.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class JobsEventPublisherImpl implements JobsEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String JOBS_TOPIC = "jobs-created-event";

    public JobsEventPublisherImpl(KafkaTemplate<String, Object> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public void publishJobsCreatedEvent(){
        try {
            kafkaTemplate.send(JOBS_TOPIC, "id:123", "job order");
            System.out.println("Successfully published the Job");
        } catch (Exception ex){
            System.err.println("Failed to publish Jobs event publisher.");
        }
    }

}
