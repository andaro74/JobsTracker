package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.ContractorDTO;
import com.andaro.jobstracker.dto.CreateContractorDTO;
import com.andaro.jobstracker.mapper.ContractorMapper;
import com.andaro.jobstracker.model.Contractor;
import com.andaro.jobstracker.repository.ContractorRepository;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ContractorServiceImpl implements ContractorService {

    private final ContractorMapper ContractorMapper;
    private final ContractorRepository contractorRepository;
    //private final ContractorEventPublisher contractorEventPublisher;

    public ContractorServiceImpl(
            ContractorMapper ContractorMapper,
            ContractorRepository contractorRepository //,
            //ContractorEventPublisher contractorEventPublisher){
    ){
        this.ContractorMapper = ContractorMapper;
        this.contractorRepository = contractorRepository;
        //this.contractorEventPublisher = contractorEventPublisher;
    }

    public Mono<ContractorDTO> getContractor(UUID id){
        Mono<Contractor> Contractor=this.contractorRepository.findContractorById(id);
        return Contractor.map(ContractorMapper::toDTO);
    }

    public Flux<List<ContractorDTO>> getAllContractors(){

        Flux<List<Contractor>> Contractors = this.contractorRepository.findAllContractors();
        return Contractors.map(ContractorMapper::toDTOs);
    }

    public Mono<ContractorDTO> updateContractor(UUID id, CreateContractorDTO createContractorDTO){
        Contractor Contractor=new Contractor();
        return Mono.just(ContractorMapper.toDTO(Contractor));
    }

    public Mono<ContractorDTO> createContractor(CreateContractorDTO createContractorDTO){
        Contractor contractor= ContractorMapper.toModel(createContractorDTO);
        contractor.setId(UUID.randomUUID());
        contractor.setCreatedOn(Instant.now());
        contractor.setModifiedOn(Instant.now());

        Mono<Contractor> contractorResult = this.contractorRepository.saveContractor(contractor).thenReturn(contractor).doOnSuccess(x ->
                {
                    System.out.println("Publishing Contractor: " + x.getContractorId());
                    //Publish the Contractor event to listeners
                    //ContractorsEventPublisher.publishContractorsCreatedEvent(x.getId().toString(), x);
                }
        );


        System.out.println("Returning from createContractor service: " + contractor.getContractorId());
        return contractorResult.map(ContractorMapper::toDTO);
    }

    public Mono<Void> deleteContractor(UUID id){
         return this.contractorRepository.deleteContractor(id);
    }

}
