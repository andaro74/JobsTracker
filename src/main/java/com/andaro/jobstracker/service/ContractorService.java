package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.ContractorDTO;
import com.andaro.jobstracker.dto.CreateContractorDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ContractorService {

    Mono<ContractorDTO> getContractor(String contractorId);

    Flux<List<ContractorDTO>> getAllContractors();

    Mono<ContractorDTO> updateContractor(String contractorId, CreateContractorDTO createContractorDTO);

    Mono<ContractorDTO> createContractor(CreateContractorDTO createContractorDTO);

    Mono<Void> deleteContractor(String contractorId);

    Flux<ContractorDTO> findContractorsByZIPCode(String zipCode);
}
