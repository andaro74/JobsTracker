package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.JobItem;
import com.andaro.jobstracker.model.JobItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Repository
public class JobsRepositoryDynamoDB implements JobsRepository {

    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    //private final DynamoDbAsyncClient dynamoDbAsyncClient;
    static final TableSchema<JobItem> jobItemTableSchema = TableSchema.fromBean(JobItem.class);

    public JobsRepositoryDynamoDB(DynamoDbAsyncClient dynamoDbAsyncClient, DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        this.dynamoDbEnhancedAsyncClient=dynamoDbEnhancedAsyncClient;
        //this.dynamoDbAsyncClient=dynamoDbAsyncClient;
    }

    public Flux<List<JobItem>> findAllJobs(){
        List<JobItem> jobItems = new ArrayList<JobItem>();

        JobItem jobItem=new JobItem();
        jobItem.setId(UUID.randomUUID());
        jobItem.setJobId(jobItem.getId().toString());
        jobItem.setJobName("BasicWash");
        jobItem.setJobUpdatedDate(LocalDateTime.now().toString());
        jobItems.add(jobItem);
        return Flux.just(jobItems);
    }

    public Mono<JobItem> saveJob(JobItem jobItem) {

        HashMap<String, AttributeValue> itemValues = new HashMap<String, AttributeValue>();

        DynamoDbAsyncTable<JobItem> jobItemTable = this.dynamoDbEnhancedAsyncClient.table("JobItem", jobItemTableSchema);

        jobItem.setId(UUID.randomUUID());
        jobItem.setJobId(jobItem.getId().toString());
        jobItem.setJobUpdatedDate(LocalDateTime.now().toString());

        try {
            jobItemTable.putItem(jobItem);
            System.out.println(("Job Item was successfully saved"));
            return Mono.just(jobItem);

        }
        catch (Exception ex){
            System.err.format("Error: " + ex);
            System.exit(1);
            throw ex;
        }

    }

    public Mono<JobItem> findJobById(UUID id){

        JobItem jobItem=new JobItem();
        jobItem.setId(UUID.randomUUID());
        jobItem.setJobId(jobItem.getId().toString());
        jobItem.setJobName("BasicCleaning");
        jobItem.setJobUpdatedDate(LocalDateTime.now().toString());
        return Mono.just(jobItem);
    }


    public void deleteJob(UUID id){
        return;
    }
}
