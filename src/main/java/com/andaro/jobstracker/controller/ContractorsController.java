package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.ContractorResponse;
import com.andaro.jobstracker.dto.ContractorRequest;
import com.andaro.jobstracker.model.ContractorSearchCriteria;
import com.andaro.jobstracker.service.ContractorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contractors")
public class ContractorsController {

    private final ContractorService contractorService;

    public ContractorsController(ContractorService contractorService){
        this.contractorService=contractorService;
    }

    @PostMapping
    public Mono<ResponseEntity<ContractorResponse>> createContractor(@RequestBody ContractorRequest contractorRequest){

        return contractorService.createContractor(contractorRequest)
                .map(contractorDTO -> new ResponseEntity<>(contractorDTO, HttpStatus.CREATED));
    }

    @GetMapping
    public Flux<ContractorResponse> getContractors(){
        return contractorService.getAllContractors();
    }

    /**
     * Retrieve a contractor by its string business identifier (contractorId),
     * e.g. "CON-0001".
     */
    @GetMapping("/{contractorId}")
    public Mono<ResponseEntity<ContractorResponse>> getContractor(@PathVariable String contractorId){
        return contractorService.getContractor(contractorId)
                .map(contractorDTO -> new ResponseEntity<>(contractorDTO, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    /**
     * Delete a contractor by its string business identifier (contractorId).
     */
    @DeleteMapping("/{contractorId}")
    public Mono<Void> deleteContractor(@PathVariable String contractorId){
        return contractorService.deleteContractor(contractorId);
    }

    @GetMapping("/search")
    public Flux<ContractorResponse> searchContractors(@RequestParam(required = false) String firstName,
                                                      @RequestParam(required = false) String lastName,
                                                      @RequestParam(required = false) String zipCode,
                                                      @RequestParam(required = false) String phoneNumber,
                                                      @RequestParam(required = false) String emailAddress,
                                                      @RequestParam(required = false) String tradeType,
                                                      @RequestParam(required = false) String address,
                                                      @RequestParam(required = false) String city,
                                                      @RequestParam(required = false) String state) {
        try {
            ContractorSearchCriteria criteria = ContractorSearchCriteria
                    .from(firstName, lastName, zipCode, phoneNumber, emailAddress, tradeType, address, city, state)
                    .requireFilters();
            return contractorService.searchContractors(criteria);
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

    @ExceptionHandler(SearchValidationException.class)
    public ResponseEntity<Map<String, Object>> handleSearchValidationErrors(SearchValidationException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(body);
    }

}
