package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.CustomerDTO;
import com.andaro.jobstracker.dto.CreateCustomerDTO;
import com.andaro.jobstracker.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    private final CustomerService customerService;

    public CustomersController(CustomerService customerService){
        this.customerService=customerService;
    }

    @PostMapping
    public Mono<ResponseEntity<CustomerDTO>> createCustomer(@RequestBody CreateCustomerDTO createCustomerDTO){

        return customerService.createCustomer(createCustomerDTO).map(CustomerDTO -> new ResponseEntity<>(CustomerDTO, HttpStatus.CREATED));
    }

    /**
     * Update a customer identified by its string business identifier (customerId),
     * e.g. "CUST-0001". This is not a UUID.
     */
    @PutMapping("/{customerId}")
    public Mono<ResponseEntity<CustomerDTO>> updateCustomer(@PathVariable String customerId, @RequestBody CreateCustomerDTO createCustomerDTO){
        return customerService.updateCustomer(customerId, createCustomerDTO)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Flux<CustomerDTO> getCustomers(){
        return customerService.getAllCustomers();
    }

    /**
     * Retrieve a customer by its string business identifier (customerId),
     * e.g. "CUST-0001".
     */
    @GetMapping("/{customerId}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomer(@PathVariable String customerId){
        return customerService.getCustomer(customerId)
                .map(CustomerDTO -> new ResponseEntity<>(CustomerDTO, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    /**
     * Delete a customer by its string business identifier (customerId).
     */
    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable String customerId){
        return customerService.deleteCustomer(customerId);
    }



}
