package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CustomerRequest;
import com.andaro.jobstracker.dto.CustomerResponse;
import com.andaro.jobstracker.mapper.CustomerMapper;
import com.andaro.jobstracker.model.Customer;
import com.andaro.jobstracker.model.CustomerKeyFactory;
import com.andaro.jobstracker.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final IdGeneratorService idGeneratorService;
    private final CustomerKeyFactory customerKeyFactory;

    public CustomerServiceImpl(
            CustomerMapper customerMapper,
            CustomerRepository customerRepository,
            IdGeneratorService idGeneratorService,
            CustomerKeyFactory customerKeyFactory
    ){
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
        this.idGeneratorService = idGeneratorService;
        this.customerKeyFactory = customerKeyFactory;
    }

    @Override
    public Mono<CustomerResponse> getCustomer(String customerId){
        return this.customerRepository.findCustomerById(customerId)
                .map(customerMapper::toDTO);
    }

    @Override
    public Flux<CustomerResponse> getAllCustomers(){
        return this.customerRepository.findAllCustomers()
                .map(customerMapper::toDTO);
    }

    @Override
    public Mono<CustomerResponse> updateCustomer(String customerId, CustomerRequest customerRequest){
        return this.customerRepository.findCustomerById(customerId)
                .flatMap(existing -> {
                    existing.setFirstName(customerRequest.firstName());
                    existing.setLastName(customerRequest.lastName());
                    existing.setAddress(customerRequest.address());
                    existing.setCity(customerRequest.city());
                    existing.setState(customerRequest.state());
                    existing.setZipCode(customerRequest.zipCode());
                    existing.setModifiedOn(Instant.now());
                    return this.customerRepository.saveCustomer(existing);
                })
                .map(customerMapper::toDTO);
    }

    @Override
    public Mono<CustomerResponse> createCustomer(CustomerRequest customerRequest){
        Customer customer= customerMapper.toModel(customerRequest);
        customer.setCustomerId(idGeneratorService.createCustomerId());
        customer.setPK(customerKeyFactory.getPartitionKey(customer.getCustomerId()));
        customer.setSK(customerKeyFactory.getSortKey(customerRequest.state(), customerRequest.city(), customerRequest.zipCode()));
        customer.setCreatedOn(Instant.now());
        customer.setModifiedOn(Instant.now());

        Mono<Customer> customerResult = this.customerRepository.saveCustomer(customer).thenReturn(customer).doOnSuccess(x ->
                {
                    System.out.println("Publishing Customer: " + x.getCustomerId());
                }
        );

        System.out.println("Returning from createCustomer service: " + customer.getCustomerId());
        return customerResult.map(customerMapper::toDTO);
    }

    @Override
    public Mono<Void> deleteCustomer(String customerId){
         return this.customerRepository.deleteCustomer(customerId);
    }

}
