package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Customer;
import com.andaro.jobstracker.model.CustomerKeyFactory;
import com.andaro.jobstracker.service.IdGeneratorService;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Repository
public class CustomerRepositoryDynamoDB implements CustomerRepository {

    private final String CUSTOMER_KEY_PREFIX="CUSTOMER#";


    private final DynamoDbAsyncTable<Customer> customerTable;
    static final TableSchema<Customer> customerTableSchema = TableSchema.fromBean(Customer.class);
    private final CustomerKeyFactory customerKeyFactory;

    public CustomerRepositoryDynamoDB(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient, CustomerKeyFactory customerKeyFactory){
        String CUSTOMER_TABLE_NAME = "Customer";
        this.customerTable = dynamoDbEnhancedAsyncClient.table(CUSTOMER_TABLE_NAME, customerTableSchema);
        this.customerKeyFactory = customerKeyFactory;
    }


    private String buildCustomerPk(String customerId) {
        return customerKeyFactory.getPartitionKey(customerId);
    }

    private String buildCustomerSk(String state, String city, String zipCode) {
        return customerKeyFactory.getSortKey(state, city, zipCode);
    }

    public Flux<Customer> findAllCustomers(){
        return Flux.from(customerTable.scan())
                .flatMap(page -> Flux.fromIterable(page.items()))
                .map(item -> {
                    Customer customer = new Customer();
                    customer.setCustomerId(item.getCustomerId());
                    customer.setFirstName(item.getFirstName());
                    customer.setLastName(item.getLastName());
                    customer.setAddress(item.getAddress());
                    customer.setCity(item.getCity());
                    customer.setState(item.getState());
                    customer.setZipCode(item.getZipCode());
                    customer.setAddress2(item.getAddress2());
                    customer.setCountry(item.getCountry());
                    customer.setEmailAddress(item.getEmailAddress());
                    customer.setPhoneNumber(item.getPhoneNumber());
                    customer.setCompanyName(item.getCompanyName());
                    customer.setCreatedOn(item.getCreatedOn());
                    customer.setModifiedOn(item.getModifiedOn());
                    return customer;
                });
    }

    public Mono<Customer> saveCustomer(Customer customer) {

        return Mono.fromFuture(customerTable.putItem(customer))
                .then(Mono.just(customer))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to put item: " + e.getMessage());
                });
    }

    public Mono<Customer> findCustomerById(String customerId){

        String partitionKey = buildCustomerPk(customerId);
        QueryConditional conditional = QueryConditional.keyEqualTo(
                Key.builder()
                        .partitionValue(partitionKey)
                        .build());

        PagePublisher<Customer> pagePublisher = customerTable.query(conditional);

        return Flux.from(pagePublisher)
                .flatMapIterable(Page::items)
                .next()
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to get item: " + e.getMessage());
                });
    }

    public Mono<Void> deleteCustomer(String customerId){
        return findCustomerById(customerId)
                .flatMap(existing -> {
                    Key key = Key.builder()
                            .partitionValue(existing.getPK())
                            .sortValue(existing.getSK())
                            .build();
                    return Mono.fromFuture(customerTable.deleteItem(key))
                            .then()
                            .doOnSuccess(ignored -> System.out.println("Completed Deleting Customer"));
                })
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed Deleting Customer: " + e.getMessage());
                })
                .then();
    }
}
