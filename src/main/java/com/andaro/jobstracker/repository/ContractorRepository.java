package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.Contractor;
import com.andaro.jobstracker.model.JobItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface ContractorRepository {

    public Flux<List<Contractor>> findAllContractors();

    public Mono<Contractor> saveContractor(Contractor item);

    public Mono<Contractor> findContractorById(UUID id);

    public Mono<Void> deleteContractor(UUID id);

}
