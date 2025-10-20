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
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;
    //private final CustomerEventPublisher CustomerEventPublisher;

    public CustomerServiceImpl(
            CustomerMapper customerMapper,
            CustomerRepository customerRepository //,
            //CustomerEventPublisher CustomerEventPublisher){
    ){
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
        //this.CustomerEventPublisher = CustomerEventPublisher;
    }

    public Mono<CustomerDTO> getCustomer(UUID id){
        Mono<Customer> customer=this.customerRepository.findCustomerById(id);
        return customer.map(customerMapper::toDTO);
    }

    public Flux<List<CustomerDTO>> getAllCustomers(){

        Flux<List<Customer>> customers = this.customerRepository.findAllCustomers();
        return customers.map(customerMapper::toDTOs);
    }

    public Mono<CustomerDTO> updateCustomer(UUID id, CreateCustomerDTO createCustomerDTO){
        Customer customer=new Customer();
        return Mono.just(customerMapper.toDTO(customer));
    }

    public Mono<CustomerDTO> createCustomer(CreateCustomerDTO createCustomerDTO){
        Customer customer= customerMapper.toModel(createCustomerDTO);
        customer.setId(UUID.randomUUID());
        customer.setCreatedOn(Instant.now());
        customer.setModifiedOn(Instant.now());

        Mono<Customer> customerResult = this.customerRepository.saveCustomer(customer).thenReturn(customer).doOnSuccess(x ->
                {
                    System.out.println("Publishing Customer: " + x.getCustomerId());
                    //Publish the Customer event to listeners
                    //CustomersEventPublisher.publishCustomersCreatedEvent(x.getId().toString(), x);
                }
        );


        System.out.println("Returning from createCustomer service: " + customer.getCustomerId());
        return customerResult.map(customerMapper::toDTO);
    }

    public Mono<Void> deleteCustomer(UUID id){
         return this.customerRepository.deleteCustomer(id);
    }

}
