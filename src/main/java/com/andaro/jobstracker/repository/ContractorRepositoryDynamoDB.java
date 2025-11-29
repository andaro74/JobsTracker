package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Contractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
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

    public ContractorRepositoryDynamoDB(DynamoDbAsyncClient dynamoDbAsyncClient, DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        String CONTRACTOR_TABLE_NAME = "Contractor";
        this.contractorTable = dynamoDbEnhancedAsyncClient.table(CONTRACTOR_TABLE_NAME, contractorTableSchema);
    }

    public Flux<List<Contractor>> findAllContractors(){
        List<Contractor> contractors = new ArrayList<>();

        PagePublisher<Contractor> results = contractorTable.scan();
        results.subscribe(x-> x
                .items().forEach(item-> {
                        System.out.println(item.getContractorId());
                        Contractor contractor=new Contractor();
                        contractor.setContractorId(item.getContractorId());
                        contractor.setPk(item.getPk());
                        contractor.setSk(item.getSk());
                        contractor.setFirstName(item.getFirstName());
                        contractor.setLastName(item.getLastName());
                        contractor.setCompanyName(item.getCompanyName());
                        contractor.setLicenseNumber(item.getLicenseNumber());
                        contractor.setTradeType(item.getTradeType());
                        contractor.setZipCode(item.getZipCode());
                        contractor.setAddress(item.getAddress());
                        contractor.setAddress2(item.getAddress2());
                        contractor.setCity(item.getCity());
                        contractor.setState(item.getState());
                        contractor.setCountry(item.getCountry());
                        contractor.setEmailAddress(item.getEmailAddress());
                        contractor.setPhoneNumber(item.getPhoneNumber());
                        contractor.setCreatedOn(item.getCreatedOn());
                        contractor.setModifiedOn(item.getModifiedOn());

                        contractors.add(contractor);
                        }
                )
        ).join();
        return Flux.just(contractors);
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

        String contractorKey= CONTRACTOR_KEY_PREFIX + contractorId;
        System.out.println("ContractorKey is " + contractorKey);
        System.out.println("Find Contractor By Id: " + contractorId);
        CompletableFuture<Contractor> future = this.contractorTable.getItem(
                Key.builder().partitionValue(contractorKey).build());
        return Mono.fromFuture(future)
                .then(Mono.justOrEmpty(future.join()))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to get contractor: " + e.getMessage());
                });
    }

    public Mono<Void> deleteContractor(String contractorId){
        String contractorKey= CONTRACTOR_KEY_PREFIX + contractorId;
        CompletableFuture<Contractor> future = this.contractorTable.deleteItem(
                Key.builder().partitionValue(contractorKey).build());
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Completed Deleting Contractor");
            }
            else {
                System.out.println("Failed Deleting Contractor");
            }
        });
        return  Mono.empty();
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
