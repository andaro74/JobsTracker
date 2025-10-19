package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.ContractorDTO;
import com.andaro.jobstracker.dto.CreateContractorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/contractors")
public class ContractorsController {

    public ContractorsController(){

    }

    @PostMapping
    public Mono<ResponseEntity<ContractorDTO>> CreateContractor(@RequestBody CreateContractorDTO input){
        ContractorDTO contractor=new ContractorDTO(UUID.randomUUID(),input.firstName(),input.lastName(),input.specialty(),input.licenseNumber(),input.zipCode(), Instant.now(),Instant.now());
        return Mono.just(new ResponseEntity<>(contractor, HttpStatus.CREATED));
    }

}
