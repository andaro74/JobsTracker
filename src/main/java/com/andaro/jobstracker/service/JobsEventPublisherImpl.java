package com.andaro.jobstracker.service;

import com.andaro.jobstracker.model.JobItem;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
public class JobsEventPublisherImpl implements JobsEventPublisher {

    private final KafkaTemplate<String, JobItemCreateEvent> kafkaTemplate;
    private static final String JOBS_TOPIC = "jobs-created-event";

    public JobsEventPublisherImpl(KafkaTemplate<String, JobItemCreateEvent> kafkaTemplate){
        this.kafkaTemplate=kafkaTemplate;
    }

    public record JobItemCreateEvent(
        String jobId,
        String jobName,
        String jobDescription,
        String customerName,
        String jobStatus,
        Instant createdOn
    ) {};

    public void publishJobsCreatedEvent(String key, JobItem jobItem){
        try {
            JobItemCreateEvent itemCreateEvent=new JobItemCreateEvent(
                    jobItem.getJobId(),
                    jobItem.getJobName(),
                    jobItem.getCustomerName(),
                    jobItem.getJobDescription(),
                    jobItem.getJobStatus(),
                    jobItem.getCreatedOn()
            );
            CompletableFuture<SendResult<String,JobItemCreateEvent>> future= kafkaTemplate.send(JOBS_TOPIC, key, itemCreateEvent);
            future.whenComplete((result, ex) -> {
               if (ex == null) {
                   System.out.println("Completed Sending to Kafka");
               }
               else {
                   System.out.println("Failed sending to Kafka");
               }
            });
        } catch (Exception ex){
            System.err.println("Failed to publish Jobs event publisher.");
        }
    }

}
