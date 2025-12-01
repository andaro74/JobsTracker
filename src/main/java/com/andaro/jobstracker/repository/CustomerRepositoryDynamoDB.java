package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Customer;
import com.andaro.jobstracker.service.IdGeneratorService;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Repository
public class CustomerRepositoryDynamoDB implements CustomerRepository {

    private final String CUSTOMER_KEY_PREFIX="CUSTOMER#";


    private final DynamoDbAsyncTable<Customer> customerTable;
    static final TableSchema<Customer> customerTableSchema = TableSchema.fromBean(Customer.class);
    private final IdGeneratorService idGeneratorService;

    public CustomerRepositoryDynamoDB(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient, IdGeneratorService idGeneratorService){
        String CUSTOMER_TABLE_NAME = "Customer";
        this.customerTable = dynamoDbEnhancedAsyncClient.table(CUSTOMER_TABLE_NAME, customerTableSchema);
        this.idGeneratorService = idGeneratorService;
    }


    private String buildCustomerPk(String customerId) {
        return DynamoKeyBuilder.buildPk(CUSTOMER_KEY_PREFIX, customerId);
    }

    private String buildCustomerSk(String state, String city, String zipCode) {
        return DynamoKeyBuilder.createAddressSortKey(state,city,zipCode);
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

        String customerKey= CUSTOMER_KEY_PREFIX + customerId;
        System.out.println("CustomerKey is " + customerKey);
        System.out.println("Find Customer By Id: " + customerId);
        CompletableFuture<Customer> future = this.customerTable.getItem(
                Key.builder().partitionValue(customerKey).build());

        return Mono.fromFuture(future)
                .then(Mono.just(future.join()))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to get item: " + e.getMessage());
                });
    }

    public Mono<Void> deleteCustomer(String customerId){
        String customerKey= CUSTOMER_KEY_PREFIX + customerId;
        CompletableFuture<Customer> future = this.customerTable.deleteItem(
                Key.builder().partitionValue(customerKey).build());
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
