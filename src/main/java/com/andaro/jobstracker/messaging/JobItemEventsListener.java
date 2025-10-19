package com.andaro.jobstracker.messaging;

import com.andaro.jobstracker.events.ContractorCreateEvent;

import com.andaro.jobstracker.events.JobItemCreateEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
public class JobItemEventsListener {
    private static final String JOBS_TOPIC = "jobs-created-event";

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

            ack.acknowledge();

        } catch (Exception ex){
            System.out.println(ex.getMessage());
            throw ex;
        }

    }
}
