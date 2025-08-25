package com.andaro.jobstracker.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.CompletableFuture;

public class DynamoDBInitializer implements ApplicationRunner {
    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    public DynamoDBInitializer(DynamoDbAsyncClient dynamoDbAsyncClient){
        this.dynamoDbAsyncClient=dynamoDbAsyncClient;
    }

    public void run(ApplicationArguments args){
        try {
            String tableName = "Jobs";
            DescribeTableRequest request = DescribeTableRequest.builder().tableName(tableName).build();
            if (request==null)
            {
                CreateTableRequest tableRequest = CreateTableRequest.builder()
                        .attributeDefinitions(
                                AttributeDefinition.builder()
                                        .attributeName("Key")
                                        .attributeType(ScalarAttributeType.S)
                                        .build(),
                                AttributeDefinition.builder()
                                        .attributeName("SortKey")
                                        .attributeType(ScalarAttributeType.S)
                                        .build())
                        .keySchema(
                                KeySchemaElement.builder()
                                        .attributeName("Key")
                                        .keyType(KeyType.HASH)
                                        .build(),
                                KeySchemaElement.builder()
                                        .attributeName("SortKey")
                                        .keyType(KeyType.RANGE)
                                        .build())
                        .provisionedThroughput(
                                ProvisionedThroughput.builder()
                                        .readCapacityUnits(new Long(10))
                                        .writeCapacityUnits(new Long(10)).build())
                        .tableName(tableName)
                        .build();
                String tableId = "";

                CompletableFuture<CreateTableResponse> result = this.dynamoDbAsyncClient.createTable(tableRequest);
                tableId =result.join().tableDescription().tableId();
                System.out.println("Table Id: " + tableId);

            }
        } catch (DynamoDbException e){
            System.err.println(e.getMessage());

        }
        System.out.println("Completed");
    }
}
