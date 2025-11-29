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

@RestController
@RequestMapping("/api/contractors")
public class ContractorsController {

    private final ContractorService contractorService;

    public ContractorsController(ContractorService contractorService){
        this.contractorService=contractorService;
    }

    @PostMapping
    public Mono<ResponseEntity<ContractorDTO>> createContractor(@RequestBody CreateContractorDTO createContractorDTO){

        return contractorService.createContractor(createContractorDTO)
                .map(contractorDTO -> new ResponseEntity<>(contractorDTO, HttpStatus.CREATED));
    }

    @GetMapping
    public Flux<List<ContractorDTO>> getContractors(){
        return contractorService.getAllContractors();
    }

    /**
     * Retrieve a contractor by its string business identifier (contractorId),
     * e.g. "CON-0001".
     */
    @GetMapping("/{contractorId}")
    public Mono<ResponseEntity<ContractorDTO>> getContractor(@PathVariable String contractorId){
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
