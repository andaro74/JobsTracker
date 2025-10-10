package com.andaro.jobstracker.initializer;

import com.andaro.jobstracker.model.JobItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.internal.waiters.ResponseOrException;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbAsyncWaiter;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.concurrent.CompletableFuture;

@Component
public class DynamoDBInitializer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBInitializer.class);
    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    static final TableSchema<JobItem> jobItemTableSchema = TableSchema.fromBean(JobItem.class);

    public DynamoDBInitializer(DynamoDbAsyncClient dynamoDbAsyncClient, DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        this.dynamoDbEnhancedAsyncClient=dynamoDbEnhancedAsyncClient;
        this.dynamoDbAsyncClient=dynamoDbAsyncClient;

    }

    @Override
    public void run(ApplicationArguments args){
        logger.info("************************************************************");
        logger.info("Starting DynamoDBInitializer in Application Runner.");
        logger.info("************************************************************");

            try {

                DynamoDbAsyncTable<JobItem> jobItemTable = this.dynamoDbEnhancedAsyncClient.table("JobItem", jobItemTableSchema);

                jobItemTable.createTable(builder -> builder
                        .provisionedThroughput(
                                ProvisionedThroughput.builder()
                                        .readCapacityUnits(new Long(10))
                                        .writeCapacityUnits(new Long(10))
                        .build())
                );

                try (DynamoDbAsyncWaiter waiter = DynamoDbAsyncWaiter.builder().client(this.dynamoDbAsyncClient).build()){
                    ResponseOrException<DescribeTableResponse> response = waiter
                            .waitUntilTableExists(builder -> builder.tableName("JobItem").build())
                            .get().matched();
                    DescribeTableResponse tableDescription = response.response().orElseThrow(
                            () -> new RuntimeException("JobItem table was not created")
                    );
                    logger.info("JobItem table was created");
                }



            } catch (ResourceNotFoundException e){
                logger.info("************************************************************");
                logger.info("Table already exists.");
                logger.info("************************************************************");
            } catch (Exception ex){
                logger.info("************************************************************");
                logger.info("Error on DynamoDBInitializer in Application Runner.");
                logger.info("************************************************************");
                System.err.println(ex.getMessage());
            }
        System.out.println("Completed");
        logger.info("************************************************************");
        logger.info("Completed DynamoDBInitializer in Application Runner.");
        logger.info("************************************************************");
    }
}
