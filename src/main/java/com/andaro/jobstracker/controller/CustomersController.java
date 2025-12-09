package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.CustomerRequest;
import com.andaro.jobstracker.dto.CustomerResponse;
import com.andaro.jobstracker.model.CustomerSearchCriteria;
import com.andaro.jobstracker.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.validation.FieldError;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomersController {

    private final CustomerService customerService;

    public CustomersController(CustomerService customerService){
        this.customerService=customerService;
    }

    @PostMapping
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(@Valid @RequestBody CustomerRequest customerRequest){

        return customerService.createCustomer(customerRequest)
                .map(customerResponse -> new ResponseEntity<>(customerResponse, HttpStatus.CREATED));
    }

    /**
     * Update a customer identified by its string business identifier (customerId),
     * e.g. "CUST-0001". This is not a UUID.
     */
    @PutMapping("/{customerId}")
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(@PathVariable String customerId, @Valid @RequestBody CustomerRequest customerRequest){
        return customerService.updateCustomer(customerId, customerRequest)
                .map(dto -> new ResponseEntity<>(dto, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public Flux<CustomerResponse> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/search")
    public Flux<CustomerResponse> searchCustomers(@RequestParam(required = false) String firstName,
                                                  @RequestParam(required = false) String lastName,
                                                  @RequestParam(required = false) String zipCode,
                                                  @RequestParam(required = false) String phoneNumber,
                                                  @RequestParam(required = false) String emailAddress) {
        try {
            CustomerSearchCriteria criteria = CustomerSearchCriteria
                    .from(firstName, lastName, zipCode, phoneNumber, emailAddress)
                    .requireFilters();
            return customerService.searchCustomers(criteria);
        } catch (IllegalArgumentException ex) {
            throw new SearchValidationException(ex.getMessage());
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private static class SearchValidationException extends RuntimeException {
        SearchValidationException(String message) {
            super(message);
        }
    }

    /**
     * Retrieve a customer by its string business identifier (customerId),
     * e.g. "CUST-0001".
     */
    @GetMapping("/{customerId}")
    public Mono<ResponseEntity<CustomerResponse>> getCustomer(@PathVariable String customerId){
        return customerService.getCustomer(customerId)
                .map(customerResponse -> new ResponseEntity<>(customerResponse, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    /**
     * Delete a customer by its string business identifier (customerId).
     */
    @DeleteMapping("/{customerId}")
    public Mono<Void> deleteCustomer(@PathVariable String customerId){
        return customerService.deleteCustomer(customerId);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(WebExchangeBindException ex) {
        List<Map<String, String>> errors = ex.getFieldErrors().stream()
                .map(this::toErrorEntry)
                .collect(Collectors.toList());

        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Validation failed: required fields missing or invalid");
        body.put("errors", errors);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(SearchValidationException.class)
    public ResponseEntity<Map<String, Object>> handleSearchValidationErrors(SearchValidationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

    private Map<String, String> toErrorEntry(FieldError fieldError) {
        Map<String, String> error = new HashMap<>();
        error.put("field", fieldError.getField());
        error.put("message", fieldError.getDefaultMessage());
        return error;
    }
}
