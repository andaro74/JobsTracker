package com.andaro.jobstracker.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbAsyncWaiter;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbWaiter;

import java.util.concurrent.CompletableFuture;

@Component
public class DynamoDBInitializer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBInitializer.class);
    private final DynamoDbAsyncClient dynamoDbAsyncClient;



    public DynamoDBInitializer(DynamoDbAsyncClient dynamoDbAsyncClient){
        this.dynamoDbAsyncClient=dynamoDbAsyncClient;
    }

    @Override
    public void run(ApplicationArguments args){
        logger.info("************************************************************");
        logger.info("Starting DynamoDBInitializer in Application Runner.");
        logger.info("************************************************************");
         String dynamoDBTableName = "Jobs";
            try {
                DescribeTableRequest request = DescribeTableRequest.builder().tableName(dynamoDBTableName).build();
                this.dynamoDbAsyncClient.describeTable(request);

                logger.info("************************************************************");
                logger.info("The table will be created");
                logger.info("************************************************************");
                CreateTableRequest tableRequest = CreateTableRequest.builder()
                        .attributeDefinitions(
                                AttributeDefinition.builder()
                                        .attributeName("PK")
                                        .attributeType(ScalarAttributeType.S)
                                        .build(),
                                AttributeDefinition.builder()
                                        .attributeName("SK")
                                        .attributeType(ScalarAttributeType.S)
                                        .build())
                        .keySchema(
                                KeySchemaElement.builder()
                                        .attributeName("PK")
                                        .keyType(KeyType.HASH)
                                        .build(),
                                KeySchemaElement.builder()
                                        .attributeName("SK")
                                        .keyType(KeyType.RANGE)
                                        .build())
                        .provisionedThroughput(
                                ProvisionedThroughput.builder()
                                        .readCapacityUnits(new Long(10))
                                        .writeCapacityUnits(new Long(10)).build())
                        .tableName(dynamoDBTableName)
                        .build();
                var result = this.dynamoDbAsyncClient.createTable(tableRequest).join();
                DynamoDbAsyncWaiter dbWaiter = this.dynamoDbAsyncClient.waiter();
                dbWaiter.waitUntilTableExists(request);
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
