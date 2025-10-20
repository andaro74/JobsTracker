package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.ContractorDTO;
import com.andaro.jobstracker.dto.CreateContractorDTO;
import com.andaro.jobstracker.service.ContractorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/contractors")
public class ContractorsController {

    private final ContractorService contractorService;

    public ContractorsController(ContractorService contractorService){
        this.contractorService=contractorService;
    }

    @PostMapping
    public Mono<ResponseEntity<ContractorDTO>> createContractor(@RequestBody CreateContractorDTO createContractorDTO){

        return contractorService.createContractor(createContractorDTO).map(contractorDTO -> new ResponseEntity<>(contractorDTO, HttpStatus.CREATED));
    }

    @GetMapping
    public Flux<List<ContractorDTO>> getContractors(){
        return contractorService.getAllContractors();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ContractorDTO>> getContractor(@PathVariable UUID id){
        return contractorService.getContractor(id)
                .map(contractorDTO -> new ResponseEntity<>(contractorDTO, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteContractor(@PathVariable UUID id){
        return contractorService.deleteContractor(id);
    }

    @GetMapping("/search")
    public Flux<ContractorDTO> searchContractors(@RequestParam(required = false) String zipCode){
        return contractorService.findContractorsByZIPCode(zipCode);
    }

//    @GetMapping("/search")
//    public Flux<ContractorDTO> searchContractors(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String zipCode,
//            @RequestParam(required = false) String specialty) {
//        return contractorService.searchContractors(name, zipCode, specialty);
//    }


}
