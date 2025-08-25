package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.JobItem;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class JobsRepositoryDynamoDB implements JobsRepository {

    private final DynamoDbAsyncClient dynamoDbClient;

    public JobsRepositoryDynamoDB(){

        Region region = Region.US_WEST_2;
        this.dynamoDbClient= DynamoDbAsyncClient.builder()
                .region(region)
                .build();
    }

    public List<JobItem> getAllJobs(){
        List<JobItem> jobItems = new ArrayList<JobItem>();
        jobItems.add(new JobItem(UUID.randomUUID(), "BasicCleaning"));
        jobItems.add(new JobItem(UUID.randomUUID(), "StandardCleaning"));
        jobItems.add(new JobItem(UUID.randomUUID(), "ProCleaning"));
        return jobItems;
    }

    public JobItem createJob(JobItem jobItem) {
        DynamoDbEnhancedAsyncClient enhancedClient = DynamoDbEnhancedAsyncClient.builder()
                .dynamoDbClient(this.dynamoDbClient)
                .build();

        return new JobItem(UUID.randomUUID(), "BasicCleaning");
    }

    public JobItem getJobById(UUID id){


        return new JobItem(UUID.randomUUID(), "BasicCleaning");
    }

    public JobItem updateJob(UUID id, JobItem item){


        return new JobItem(UUID.randomUUID(), "BasicCleaning");
    }

    public void deleteJob(UUID id){

        return;
    }
}
