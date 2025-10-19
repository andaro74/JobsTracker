package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.ContractorDTO;
import com.andaro.jobstracker.dto.CreateContractorDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ContractorService {

    public Mono<ContractorDTO> getContractor(UUID id);

    public Flux<List<ContractorDTO>> getAllContractors();

    public Mono<ContractorDTO> updateContractor(UUID id, CreateContractorDTO createContractorDTO);

    public Mono<ContractorDTO> createContractor(CreateContractorDTO createContractorDTO);

    public Mono<Void> deleteContractor(UUID id);
}
