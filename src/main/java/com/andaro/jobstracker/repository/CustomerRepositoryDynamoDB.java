package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Customer;
import com.andaro.jobstracker.model.CustomerKeyFactory;
import com.andaro.jobstracker.model.CustomerSearchCriteria;
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
        return scanAllCustomers();
    }

    private Flux<Customer> scanAllCustomers() {
        return Flux.from(customerTable.scan())
                .flatMap(page -> Flux.fromIterable(page.items()))
                .map(this::projectCustomer);
    }

    private Customer projectCustomer(Customer item) {
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
    }

    public Mono<Customer> saveCustomer(Customer customer) {

        return Mono.fromFuture(customerTable.putItem(customer))
                .then(Mono.just(customer))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to put item: " + e.getMessage());
                });
    }

    @Override
    public Mono<Customer> updateCustomer(Customer customer, String previousSortKey) {
        Mono<Void> deleteMono = Mono.empty();
        if (previousSortKey != null && !previousSortKey.equals(customer.getSK())) {
            Key deleteKey = Key.builder()
                    .partitionValue(customer.getPK())
                    .sortValue(previousSortKey)
                    .build();
            deleteMono = Mono.fromFuture(customerTable.deleteItem(deleteKey)).then();
        }

        return deleteMono
                .then(Mono.fromFuture(customerTable.putItem(customer)))
                .thenReturn(customer)
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed updating customer: " + e.getMessage());
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

    @Override
    public Flux<Customer> searchCustomers(CustomerSearchCriteria criteria) {
        return Flux.from(customerTable.scan())
                .flatMap(page -> Flux.fromIterable(page.items()))
                .filter(customer -> matchesCriteria(customer, criteria))
                .map(this::projectCustomer);
    }

    private boolean matchesCriteria(Customer customer, CustomerSearchCriteria criteria) {
        if (criteria.getFirstName() != null && !equalsIgnoreCase(criteria.getFirstName(), customer.getFirstName())) {
            return false;
        }
        if (criteria.getLastName() != null && !equalsIgnoreCase(criteria.getLastName(), customer.getLastName())) {
            return false;
        }
        if (criteria.getZipCode() != null && !equalsIgnoreCase(criteria.getZipCode(), customer.getZipCode())) {
            return false;
        }
        if (criteria.getPhoneNumber() != null && !equalsIgnoreCase(criteria.getPhoneNumber(), customer.getPhoneNumber())) {
            return false;
        }
        if (criteria.getEmailAddress() != null && !equalsIgnoreCase(criteria.getEmailAddress(), customer.getEmailAddress())) {
            return false;
        }
        return true;
    }

    private boolean equalsIgnoreCase(String expected, String actual) {
        return actual != null && actual.equalsIgnoreCase(expected);
    }
}
