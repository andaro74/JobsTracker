package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CustomerRepository {

    public Flux<List<Customer>> findAllCustomers();

    public Mono<Customer> saveCustomer(Customer item);

    public Mono<Customer> findCustomerById(UUID id);

    public Mono<Void> deleteCustomer(UUID id);

}
