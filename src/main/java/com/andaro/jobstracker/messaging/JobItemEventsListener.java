package com.andaro.jobstracker.messaging;

import com.andaro.jobstracker.events.ContractorCreateEvent;

import com.andaro.jobstracker.events.JobItemCreateEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import com.andaro.jobstracker.service.JobItemEventOrchestrator;

@Component
public class JobItemEventsListener {
    private static final String JOBS_TOPIC = "jobs-created-event";

    private final JobItemEventOrchestrator jobItemEventOrchestrator;

    public JobItemEventsListener(JobItemEventOrchestrator jobItemEventOrchestrator){
        this.jobItemEventOrchestrator = jobItemEventOrchestrator;
    }

    @KafkaListener(
            topics = JOBS_TOPIC,
            containerFactory = "jobItemCreateEventConcurrentKafkaListenerContainerFactory"
    )
    public void onJobItemCreated(
            JobItemCreateEvent event,
            @Header(KafkaHeaders.RECEIVED_KEY) String key,
            ConsumerRecord<String, JobItemCreateEvent> rawRecord,
            Acknowledgment ack
    ){
        try
        {
            System.out.println("onJobItemCreated: " + event);
            //TODO: Assign an available contractor and schedule work.

            this.jobItemEventOrchestrator.processNewJobItem(event);


            ack.acknowledge();

        } catch (Exception ex){
            System.out.println(ex.getMessage());
            throw ex;
        }

    }
}
