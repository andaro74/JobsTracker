package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CustomerDTO;
import com.andaro.jobstracker.dto.CreateCustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    public Mono<CustomerDTO> getCustomer(UUID id);

    public Flux<List<CustomerDTO>> getAllCustomers();

    public Mono<CustomerDTO> updateCustomer(UUID id, CreateCustomerDTO createCustomerDTO);

    public Mono<CustomerDTO> createCustomer(CreateCustomerDTO createCustomerDTO);

    public Mono<Void> deleteCustomer(UUID id);
}
