package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.ContractorResponse;
import com.andaro.jobstracker.dto.ContractorRequest;
import com.andaro.jobstracker.model.ContractorSearchCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ContractorService {

    Mono<ContractorResponse> getContractor(String contractorId);

    Flux<ContractorResponse> getAllContractors();

    Mono<ContractorResponse> updateContractor(String contractorId, ContractorRequest contractorRequest);

    Mono<ContractorResponse> createContractor(ContractorRequest contractorRequest);

    Mono<Void> deleteContractor(String contractorId);

    Flux<ContractorResponse> findContractorsByZIPCode(String zipCode);

    Flux<ContractorResponse> searchContractors(ContractorSearchCriteria criteria);
}
