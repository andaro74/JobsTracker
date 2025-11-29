package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CustomerRepository {

    public Flux<List<Customer>> findAllCustomers();

    public Mono<Customer> saveCustomer(Customer item);

    public Mono<Customer> findCustomerById(String customerId);

    public Mono<Void> deleteCustomer(String customerId);

}
