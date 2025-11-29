package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CustomerDTO;
import com.andaro.jobstracker.dto.CreateCustomerDTO;
import com.andaro.jobstracker.mapper.CustomerMapper;
import com.andaro.jobstracker.model.Customer;
import com.andaro.jobstracker.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    private final IdGeneratorService idGeneratorService;

    public CustomerServiceImpl(
            CustomerMapper customerMapper,
            CustomerRepository customerRepository,
            IdGeneratorService idGeneratorService
    ){
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
        this.idGeneratorService = idGeneratorService;
    }

    @Override
    public Mono<CustomerDTO> getCustomer(String customerId){
        return this.customerRepository.findCustomerById(customerId)
                .map(customerMapper::toDTO);
    }

    @Override
    public Flux<List<CustomerDTO>> getAllCustomers(){
        Flux<List<Customer>> customers = this.customerRepository.findAllCustomers();
        return customers.map(customerMapper::toDTOs);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(String customerId, CreateCustomerDTO createCustomerDTO){
        return this.customerRepository.findCustomerById(customerId)
                .flatMap(existing -> {
                    existing.setFirstName(createCustomerDTO.firstName());
                    existing.setLastName(createCustomerDTO.lastName());
                    existing.setAddress(createCustomerDTO.address());
                    existing.setCity(createCustomerDTO.city());
                    existing.setState(createCustomerDTO.state());
                    existing.setZipCode(createCustomerDTO.zipCode());
                    existing.setModifiedOn(Instant.now());
                    return this.customerRepository.saveCustomer(existing);
                })
                .map(customerMapper::toDTO);
    }

    @Override
    public Mono<CustomerDTO> createCustomer(CreateCustomerDTO createCustomerDTO){
        Customer customer= customerMapper.toModel(createCustomerDTO);
        // Generate business customerId and wire PK/SK via setter
        String newCustomerId = idGeneratorService.createCustomerId();
        customer.setCustomerId(newCustomerId);
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
