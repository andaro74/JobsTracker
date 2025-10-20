package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.CustomerDTO;
import com.andaro.jobstracker.dto.CreateCustomerDTO;
import com.andaro.jobstracker.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

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

    @GetMapping
    public Flux<List<CustomerDTO>> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomer(@PathVariable UUID id){
        return customerService.getCustomer(id)
                .map(CustomerDTO -> new ResponseEntity<>(CustomerDTO, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable UUID id){
        return customerService.deleteCustomer(id);
    }



}
