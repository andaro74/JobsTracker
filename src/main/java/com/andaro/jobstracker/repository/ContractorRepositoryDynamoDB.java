package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Contractor;
import com.andaro.jobstracker.model.JobItem;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
        List<Contractor> contractors = new ArrayList<Contractor>();

        PagePublisher<Contractor> results = contractorTable.scan();
        results.subscribe(x-> x
                .items().stream()
                .forEach(item-> {
                        System.out.println(item.getContractorId());
                        Contractor contractor=new Contractor();
                        contractor.setId(item.getId());
                        contractor.setFirstName(item.getFirstName());
                        contractor.setLastName(item.getLastName());
                        contractor.setCompanyName(item.getCompanyName());
                        contractor.setLicenseNumber(item.getLicenseNumber());
                        contractor.setSpecialty(item.getSpecialty());
                        contractor.setZipCode(item.getZipCode());
                        contractor.setCreatedOn(item.getCreatedOn());
                        contractor.setModifiedOn(item.getModifiedOn());

                        contractors.add(contractor);
                        }
                )
                )
                .join();
        return Flux.just(contractors);
    }

    public Mono<Contractor> saveContractor(Contractor contractor) {
        contractor.setId(UUID.randomUUID());
        contractor.setContractorId(contractor.getId().toString());
        contractor.setModifiedOn(Instant.now());
        System.out.println("Saving Contractor: " + contractor);

        return Mono.fromFuture(contractorTable.putItem(contractor))
                .then(Mono.just(contractor))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to put item: " + e.getMessage());
                });
    }

    public Mono<Contractor> findContractorById(UUID id){

        String contractorKey= CONTRACTOR_KEY_PREFIX + id.toString();
        System.out.println("ContractorKey is " + contractorKey);
        System.out.println("Find Contractor By Id: " + id.toString());
        CompletableFuture<Contractor> future = this.contractorTable.getItem(Key.builder().partitionValue(contractorKey).build());
        Contractor contractor=future.join();
        System.out.println("Retrieved Contractor: " + contractor);
        return Mono.justOrEmpty(contractor);
    }

    public Mono<Void> deleteContractor(UUID id){
        String contractorKey= CONTRACTOR_KEY_PREFIX + id.toString();
        CompletableFuture<Contractor> future = this.contractorTable.deleteItem(Key.builder().partitionValue(contractorKey).build());
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
