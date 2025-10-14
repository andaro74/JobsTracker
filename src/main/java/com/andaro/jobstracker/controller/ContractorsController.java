package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.ContractorDTO;
import com.andaro.jobstracker.dto.CreateContractorDTO;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
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
        ContractorDTO contractor=new ContractorDTO(UUID.randomUUID(),input.FirstName(),input.LastName(),input.Specialty(),input.LicenseNumber(),input.ZipCode(), Instant.now(),Instant.now());
        return Mono.just(new ResponseEntity<>(contractor, HttpStatus.CREATED));
    }

}
