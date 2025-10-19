package com.andaro.jobstracker.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbAsyncWaiter;


import java.net.URI;

@Configuration
public class DynamoDBConfig {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBConfig.class);

    @Bean
    public DynamoDbAsyncClient dynamoDbAsyncClient(){

        logger.info("************************************************************");
        logger.info("Setting DynamoDBConfig DynamoDBAsyncClient configuration.");
        logger.info("************************************************************");

        return DynamoDbAsyncClient.builder()
                .endpointOverride(URI.create("http://dynamodb-local:8000")) //local DynamoDB
                .region(Region.US_WEST_2)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("dummy", "dummy")
                ))
                .build();

    }


    @Bean
    public DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient(DynamoDbAsyncClient client){
        logger.info("************************************************************");
        logger.info("Setting DynamoDbEnhancedAsyncClient.");
        logger.info("************************************************************");
        return DynamoDbEnhancedAsyncClient
                .builder()
                .dynamoDbClient(client)
                .build();

    }

    @Bean
    public DynamoDbAsyncWaiter dynamoDbAsyncWaiter(DynamoDbAsyncClient client){
        //The waiter is a utility of the base client
        logger.info("************************************************************");
        logger.info("Executing dynamoDbAsyncWaiter.");
        logger.info("************************************************************");
        return client.waiter();
    }
}
