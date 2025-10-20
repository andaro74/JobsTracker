package com.andaro.jobstracker.initializer;

import com.andaro.jobstracker.model.Catalog;
import com.andaro.jobstracker.model.Contractor;
import com.andaro.jobstracker.model.Customer;
import com.andaro.jobstracker.model.JobItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.waiters.DynamoDbAsyncWaiter;

@Component
public class DynamoDBInitializer implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(DynamoDBInitializer.class);
    private final DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient;
    private final DynamoDbAsyncWaiter waiter;



    public DynamoDBInitializer(
            DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient,
            DynamoDbAsyncWaiter waiter
    ){
        this.dynamoDbEnhancedAsyncClient=dynamoDbEnhancedAsyncClient;
        this.waiter=waiter;
    }

    @Override
    public void run(ApplicationArguments args){
        logger.info("************************************************************");
        logger.info("Starting DynamoDBInitializer in Application Runner.");
        logger.info("************************************************************");
        String JOB_ITEM_TABLE_NAME="JobItem";
        String CONTRACTOR_TABLE_NAME="Contractor";
        String CUSTOMER_TABLE_NAME="Customer";
        String CATALOG_TABLE_NAME="Catalog";

        TableSchema<JobItem> jobItemTableSchema = TableSchema.fromBean(JobItem.class);
        TableSchema<Contractor> contractorTableSchema = TableSchema.fromBean(Contractor.class);
        TableSchema<Customer> customerTableSchema = TableSchema.fromBean(Customer.class);
        TableSchema<Catalog> catalogTableSchema = TableSchema.fromBean(Catalog.class);

        DynamoDbAsyncTable<JobItem> jobItemTable = this.dynamoDbEnhancedAsyncClient.table(JOB_ITEM_TABLE_NAME, jobItemTableSchema);
        DynamoDbAsyncTable<Contractor> contractorTable = this.dynamoDbEnhancedAsyncClient.table(CONTRACTOR_TABLE_NAME, contractorTableSchema);
        DynamoDbAsyncTable<Customer> customerTable = this.dynamoDbEnhancedAsyncClient.table(CUSTOMER_TABLE_NAME, customerTableSchema);
        DynamoDbAsyncTable<Catalog> catalogTable = this.dynamoDbEnhancedAsyncClient.table(CATALOG_TABLE_NAME, catalogTableSchema);

        Mono<Void> createContractor = createTableAndWait(contractorTable);
        Mono<Void> createJobItem = createTableAndWait(jobItemTable);
        Mono<Void> createCustomer = createTableAndWait(customerTable);
        Mono<Void> createCatalog = createTableAndWait(catalogTable);

        Mono.when(createContractor, createJobItem, createCustomer, createCatalog)
                .doOnSuccess(v->logger.info("All DynamoDB tables are initialized and ACTIVE."))
                .doOnError(e->logger.error("Failed to initialize one or more tables", e))
                .block();

        logger.info("DynamoDB initialization complete");

        System.out.println("Completed");
        logger.info("************************************************************");
        logger.info("Completed DynamoDBInitializer in Application Runner.");
        logger.info("************************************************************");
    }


    private <T> Mono<Void> createTableAndWait(DynamoDbAsyncTable<T> table){
        String tableName = table.tableName();

        Mono<Void> createMono = Mono.fromFuture(table.createTable())
                .doOnSuccess(v-> logger.info("Sent CREATE request for table: {}", tableName))
                .onErrorResume(ResourceInUseException.class, e->{
                    logger.warn("Table {} already exists, skipping creation.", tableName);
                    return Mono.empty();
                });

        Mono<WaiterResponse<DescribeTableResponse>> waitMono = Mono.fromFuture(
                waiter.waitUntilTableExists(b->b.tableName(tableName))
        );

        return createMono
                .then(waitMono)
                .doOnSuccess(waitRes -> logger.info("Table {} is now ACTIVE.", tableName))
                .then();
    }
}
