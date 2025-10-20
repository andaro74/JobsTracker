package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Repository
public class CustomerRepositoryDynamoDB implements CustomerRepository {

    private static final Logger log = LoggerFactory.getLogger(CustomerRepositoryDynamoDB.class);
    private final String CUSTOMER_KEY_PREFIX="CustomerNumber#";

    private final DynamoDbAsyncTable<Customer> customerTable;
    static final TableSchema<Customer> customerTableSchema = TableSchema.fromBean(Customer.class);

    public CustomerRepositoryDynamoDB(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        String CUSTOMER_TABLE_NAME = "Customer";
        this.customerTable = dynamoDbEnhancedAsyncClient.table(CUSTOMER_TABLE_NAME, customerTableSchema);
    }

    public Flux<List<Customer>> findAllCustomers(){
        List<Customer> customers = new ArrayList<Customer>();

        PagePublisher<Customer> results = customerTable.scan();
        results.subscribe(x-> x
                .items().stream()
                .forEach(item-> {
                        System.out.println(item.getCustomerId());
                        Customer customer=new Customer();
                        customer.setId(item.getId());
                        customer.setFirstName(item.getFirstName());
                        customer.setLastName(item.getLastName());
                        customer.setAddress(item.getAddress());
                        customer.setCity(item.getCity());
                        customer.setState(item.getState());
                        customer.setZipCode(item.getZipCode());
                        customer.setCreatedOn(item.getCreatedOn());
                        customer.setModifiedOn(item.getModifiedOn());

                        customers.add(customer);
                        }
                )
                )
                .join();
        return Flux.just(customers);
    }

    public Mono<Customer> saveCustomer(Customer customer) {
        customer.setId(UUID.randomUUID());
        customer.setCustomerId(customer.getId().toString());
        customer.setModifiedOn(Instant.now());
        System.out.println("Saving Customer: " + customer);

        return Mono.fromFuture(customerTable.putItem(customer))
                .then(Mono.just(customer))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to put item: " + e.getMessage());
                });
    }

    public Mono<Customer> findCustomerById(UUID id){

        String customerKey= CUSTOMER_KEY_PREFIX + id.toString();
        System.out.println("CustomerKey is " + customerKey);
        System.out.println("Find Customer By Id: " + id.toString());
        CompletableFuture<Customer> future = this.customerTable.getItem(Key.builder().partitionValue(customerKey).build());
        //Customer customer=future.join();
        //System.out.println("Retrieved Customer: " + customer);
        //return Mono.justOrEmpty(customer);

        return Mono.fromFuture(future)
                .then(Mono.just(future.join()))
                .doOnError(DynamoDbException.class, e -> {
                   System.err.println("Failed to get item: " + e.getMessage());
                });
    }

    public Mono<Void> deleteCustomer(UUID id){
        String customerKey= CUSTOMER_KEY_PREFIX + id.toString();
        CompletableFuture<Customer> future = this.customerTable.deleteItem(Key.builder().partitionValue(customerKey).build());
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Completed Deleting Customer");
            }
            else {
                System.out.println("Failed Deleting Customer");
            }
        });
        return  Mono.empty();
    }
}
