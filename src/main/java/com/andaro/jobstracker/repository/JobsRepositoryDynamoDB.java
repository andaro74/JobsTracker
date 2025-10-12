package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.JobItem;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Repository
public class JobsRepositoryDynamoDB implements JobsRepository {

    private final String JOB_ITEM_KEY_PREFIX="JobNumber#";

    private final DynamoDbAsyncTable<JobItem> jobItemTable;
    static final TableSchema<JobItem> jobItemTableSchema = TableSchema.fromBean(JobItem.class);

    public JobsRepositoryDynamoDB(DynamoDbAsyncClient dynamoDbAsyncClient, DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        String JOB_ITEM_TABLE_NAME = "JobItem";
        this.jobItemTable = dynamoDbEnhancedAsyncClient.table(JOB_ITEM_TABLE_NAME, jobItemTableSchema);
    }

    public Flux<List<JobItem>> findAllJobs(){
        List<JobItem> jobItems = new ArrayList<JobItem>();

        PagePublisher<JobItem> results = jobItemTable.scan();
        results.subscribe(x-> x
                .items().stream()
                .forEach(item-> {
                        System.out.println(item.getJobId());
                        JobItem jobItem=new JobItem();
                        jobItem.setId(item.getId());
                        jobItem.setJobId(item.getJobId());
                        jobItem.setJobName(item.getJobName());
                        jobItem.setCustomerName(item.getCustomerName());
                        jobItem.setJobDescription(item.getJobDescription());
                        jobItem.setJobUpdatedDate(item.getJobUpdatedDate());
                        jobItem.setCreatedOn(item.getCreatedOn());
                        jobItem.setJobStatus(item.getJobStatus());
                        jobItems.add(jobItem);
                        }
                )
                )
                .join();
        return Flux.just(jobItems);
    }

    public Mono<JobItem> saveJob(JobItem jobItem) {
        jobItem.setId(UUID.randomUUID());
        jobItem.setJobId(jobItem.getId().toString());
        jobItem.setJobUpdatedDate(LocalDateTime.now().toString());
        CompletableFuture<Void> future = jobItemTable.putItem(jobItem);
        System.out.println("Job Item was successfully saved");
        return Mono.justOrEmpty(jobItem);
    }

    public Mono<JobItem> findJobById(UUID id){

        String jobItemKey= JOB_ITEM_KEY_PREFIX + id.toString();
        System.out.println("JobItemKey is " + jobItemKey);
        System.out.println("Find Job By Id: " + id.toString());
        CompletableFuture<JobItem> future = this.jobItemTable.getItem(Key.builder().partitionValue(jobItemKey).build());
        JobItem jobItem=future.join();
        System.out.println("Retrieved JobItem " + jobItem);
        return Mono.justOrEmpty(jobItem);
    }

    public void deleteJob(UUID id){
        String jobItemKey=JOB_ITEM_KEY_PREFIX + id.toString();
        CompletableFuture<JobItem> future = this.jobItemTable.deleteItem(Key.builder().partitionValue(jobItemKey).build());
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Completed Deleting Item");
            }
            else {
                System.out.println("Failed Deleting Item");
            }
        });
    }
}
