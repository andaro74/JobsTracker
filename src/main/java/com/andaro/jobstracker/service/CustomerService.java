package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CustomerRequest;
import com.andaro.jobstracker.dto.CustomerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    public Mono<CustomerResponse> getCustomer(String customerId);

    public Flux<CustomerResponse> getAllCustomers();

    public Mono<CustomerResponse> updateCustomer(String customerId, CustomerRequest customerRequest);

    public Mono<CustomerResponse> createCustomer(CustomerRequest customerRequest);

    public Mono<Void> deleteCustomer(String customerId);
}
