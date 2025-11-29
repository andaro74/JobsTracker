package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.JobItem;
import com.andaro.jobstracker.service.IdGeneratorService;
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

    private final String JOB_ITEM_KEY_PREFIX="JOB#";
    private final String CUSTOMER_KEY_PREFIX="CUSTOMER#";

    private final DynamoDbAsyncTable<JobItem> jobItemTable;
    static final TableSchema<JobItem> jobItemTableSchema = TableSchema.fromBean(JobItem.class);
    private final IdGeneratorService idGeneratorService;

    public JobsRepositoryDynamoDB(DynamoDbAsyncClient dynamoDbAsyncClient, DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient, IdGeneratorService idGeneratorService){
        String JOB_ITEM_TABLE_NAME = "JobItem";
        this.jobItemTable = dynamoDbEnhancedAsyncClient.table(JOB_ITEM_TABLE_NAME, jobItemTableSchema);
        this.idGeneratorService = idGeneratorService;
    }

    public Flux<List<JobItem>> findAllJobs(){
        List<JobItem> jobItems = new ArrayList<>();

        PagePublisher<JobItem> results = jobItemTable.scan();
        results.subscribe(x-> x
                .items().forEach(item-> {
                        System.out.println(item.getJobId());
                        JobItem jobItem=new JobItem();
                        jobItem.setJobId(item.getJobId());
                        jobItem.setCatalogId(item.getCatalogId());
                        jobItem.setCustomerId(item.getCustomerId());
                        jobItem.setContractorId(item.getContractorId());
                        jobItem.setJobDescription(item.getJobDescription());
                        jobItem.setJobUpdatedDate(item.getJobUpdatedDate());
                        jobItem.setCreatedOn(item.getCreatedOn());
                        jobItem.setJobStatus(item.getJobStatus());
                        jobItem.setExpectedCompletion(item.getExpectedCompletion());
                        jobItem.setActualCompletion(item.getActualCompletion());
                        jobItems.add(jobItem);
                        }
                )
                )
                .join();
        return Flux.just(jobItems);
    }

    public Mono<JobItem> saveJob(JobItem jobItem) {
        if (jobItem.getJobId() == null) {
            String newJobId = idGeneratorService.createJobId();
            jobItem.setJobId(newJobId);
            jobItem.setPk(JOB_ITEM_KEY_PREFIX + newJobId);
        }
        if (jobItem.getCustomerId() != null) {
            jobItem.setSk(CUSTOMER_KEY_PREFIX + jobItem.getCustomerId());
        }
        jobItem.setJobUpdatedDate(LocalDateTime.now().toString());
        CompletableFuture<Void> future = jobItemTable.putItem(jobItem);
        System.out.println("Job Item was successfully saved");
        return Mono.justOrEmpty(jobItem);
    }

    public Mono<JobItem> findJobById(String jobId){

        String jobItemKey= JOB_ITEM_KEY_PREFIX + jobId;
        System.out.println("JobItemKey is " + jobItemKey);
        System.out.println("Find Job By Id: " + jobId);
        CompletableFuture<JobItem> future = this.jobItemTable.getItem(Key.builder().partitionValue(jobItemKey).build());
        JobItem jobItem=future.join();
        System.out.println("Retrieved JobItem " + jobItem);
        return Mono.justOrEmpty(jobItem);
    }

    public void deleteJob(String jobId){
        String jobItemKey=JOB_ITEM_KEY_PREFIX + jobId;
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
