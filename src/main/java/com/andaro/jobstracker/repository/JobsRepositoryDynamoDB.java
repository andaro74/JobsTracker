package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.JobItem;
import com.andaro.jobstracker.model.JobItemRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Repository
public class JobsRepositoryDynamoDB implements JobsRepository {

    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    public JobsRepositoryDynamoDB(DynamoDbAsyncClient dynamoDbAsyncClient){
        this.dynamoDbAsyncClient=dynamoDbAsyncClient;

    }

    public Flux<List<JobItem>> findAllJobs(){
        List<JobItem> jobItems = new ArrayList<JobItem>();
        jobItems.add(new JobItem(UUID.randomUUID(), "BasicCleaning"));
        jobItems.add(new JobItem(UUID.randomUUID(), "StandardCleaning"));
        jobItems.add(new JobItem(UUID.randomUUID(), "ProCleaning"));
        return Flux.just(jobItems);
    }

    public Mono<JobItem> saveJob(JobItem jobItem) {

        HashMap<String, AttributeValue> itemValues = new HashMap<String, AttributeValue>();

        JobItem item =new JobItem(UUID.randomUUID(), jobItem.getJobName());

        itemValues.put("PK", AttributeValue.builder().s(item.getId().toString()).build());
        itemValues.put("SK",AttributeValue.builder().s(item.getJobName()).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName("Jobs")
                .item(itemValues)
                .build();

        try {

            //https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/java_dynamodb_code_examples.html
            CompletableFuture<PutItemResponse> response= this.dynamoDbAsyncClient.putItem(request);

            return  Mono.fromFuture(response).thenReturn(item);



            /*
            future.handle((response, throwable)->{
                if (response!=null){
                    System.out.println("Updated DynamoDB record via this request: " + response);
                    return response.attributes();
                } else {
                  RuntimeException cause = (RuntimeException) throwable.getCause();
                  System.out.println(cause.getMessage());
                  throw cause;
                }
            });
            System.out.println(("Jobs was successfully uploaded"));
            */


        }
        catch (Exception ex){
            System.err.format("Error: " + ex);
            System.exit(1);
            throw ex;
        }

    }

    public Mono<JobItem> findJobById(UUID id){

        return Mono.just(new JobItem(UUID.randomUUID(), "BasicCleaning"));
    }


    public void deleteJob(UUID id){

        return;
    }
}
