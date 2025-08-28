package com.andaro.jobstracker.initializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.concurrent.CompletableFuture;

public class DynamoDBInitializer implements ApplicationRunner {
    private final DynamoDbAsyncClient dynamoDbAsyncClient;

    @Value("${DYNAMODB_TABLE}")
    private String dynamoDBTableName;

    public DynamoDBInitializer(DynamoDbAsyncClient dynamoDbAsyncClient){
        this.dynamoDbAsyncClient=dynamoDbAsyncClient;
    }

    public void run(ApplicationArguments args){
        try {

            DescribeTableRequest request = DescribeTableRequest.builder().tableName(dynamoDBTableName).build();
            if (request==null)
            {
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
