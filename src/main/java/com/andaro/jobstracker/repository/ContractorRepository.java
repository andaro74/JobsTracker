package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.Contractor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ContractorRepository {

    Flux<List<Contractor>> findAllContractors();

    Mono<Contractor> saveContractor(Contractor item);

    Mono<Contractor> findContractorById(String contractorId);

    Mono<Void> deleteContractor(String contractorId);

    Flux<Contractor> findContractorsByZIPCode(String zipCode);

}
