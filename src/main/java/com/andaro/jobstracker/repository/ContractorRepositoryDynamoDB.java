package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Contractor;
import com.andaro.jobstracker.model.ContractorKeyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.enhanced.dynamodb.Expression;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Repository
public class ContractorRepositoryDynamoDB implements ContractorRepository {

    private static final Logger log = LoggerFactory.getLogger(ContractorRepositoryDynamoDB.class);
    private final String CONTRACTOR_KEY_PREFIX="ContractorNumber#";

    private final DynamoDbAsyncTable<Contractor> contractorTable;
    static final TableSchema<Contractor> contractorTableSchema = TableSchema.fromBean(Contractor.class);
    private final ContractorKeyFactory contractorKeyFactory;

    public ContractorRepositoryDynamoDB(DynamoDbAsyncClient dynamoDbAsyncClient, DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient, ContractorKeyFactory contractorKeyFactory){
        String CONTRACTOR_TABLE_NAME = "Contractor";
        this.contractorTable = dynamoDbEnhancedAsyncClient.table(CONTRACTOR_TABLE_NAME, contractorTableSchema);
        this.contractorKeyFactory = contractorKeyFactory;
    }

    private String buildContractorPk(String contractorId) {
        return contractorKeyFactory.getPartitionKey(contractorId);
    }

    public Flux<Contractor> findAllContractors(){
        // Stream scan results directly as Flux<Contractor>
        return Flux.from(contractorTable.scan().items());
    }

    public Mono<Contractor> saveContractor(Contractor contractor) {
        // If contractorId is not yet set, the service should provide it via setContractorId.
        // Here we only ensure timestamps are updated.
        contractor.setModifiedOn(Instant.now());
        System.out.println("Saving Contractor: " + contractor);

        return Mono.fromFuture(contractorTable.putItem(contractor))
                .then(Mono.just(contractor))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to put item: " + e.getMessage());
                });
    }

    public Mono<Contractor> findContractorById(String contractorId){

        String partitionKey = buildContractorPk(contractorId);
        QueryConditional conditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(partitionKey)
                        .build());

        PagePublisher<Contractor> pagePublisher = contractorTable.query(conditional);

        return Flux.from(pagePublisher)
                .flatMapIterable(page -> page.items())
                .next()
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to get contractor: " + e.getMessage());
                });
    }

    public Mono<Void> deleteContractor(String contractorId){
        return findContractorById(contractorId)
                .flatMap(existing -> {
                    Key key = Key.builder()
                            .partitionValue(existing.getPk())
                            .sortValue(existing.getSk())
                            .build();
                    return Mono.fromFuture(contractorTable.deleteItem(key))
                            .then()
                            .doOnSuccess(ignored -> System.out.println("Completed Deleting Contractor"));
                })
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed Deleting Contractor: " + e.getMessage());
                })
                .then();
    }
    
    public Flux<Contractor> findContractorsByZIPCode(String zipCode){
        Expression filterExpression = Expression.builder()
                .expression("zipCode = :zipCode")
                .putExpressionValue(":zipCode", AttributeValue.builder().s(zipCode).build())
                .build();

        return Flux.from(contractorTable.scan(builder -> builder.filterExpression(filterExpression))
                .items());
    }
}
