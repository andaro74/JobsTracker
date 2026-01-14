package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Contractor;
import com.andaro.jobstracker.model.ContractorSearchCriteria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ContractorRepository {

    Flux<Contractor> findAllContractors();

    Mono<Contractor> saveContractor(Contractor item);

    Mono<Contractor> findContractorById(String contractorId);

    Mono<Void> deleteContractor(String contractorId);

    Flux<Contractor> findContractorsByZIPCode(String zipCode);

    Flux<Contractor> searchContractors(ContractorSearchCriteria criteria);

}
