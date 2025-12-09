package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.Customer;
import com.andaro.jobstracker.model.CustomerSearchCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerRepository {

    public Flux<Customer> findAllCustomers();

    public Mono<Customer> saveCustomer(Customer item);

    public Mono<Customer> updateCustomer(Customer item, String previousSortKey);

    public Mono<Customer> findCustomerById(String customerId);

    public Mono<Void> deleteCustomer(String customerId);

    public Flux<Customer> searchCustomers(CustomerSearchCriteria criteria);

}
