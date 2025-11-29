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

@Service
public class ContractorServiceImpl implements ContractorService {

    private final ContractorMapper ContractorMapper;
    private final ContractorRepository contractorRepository;
    private final IdGeneratorService idGeneratorService;
    //private final ContractorEventPublisher contractorEventPublisher;

    public ContractorServiceImpl(
            ContractorMapper ContractorMapper,
            ContractorRepository contractorRepository,
            IdGeneratorService idGeneratorService//,
            //ContractorEventPublisher contractorEventPublisher){
    ){
        this.ContractorMapper = ContractorMapper;
        this.contractorRepository = contractorRepository;
        this.idGeneratorService = idGeneratorService;
        //this.contractorEventPublisher = contractorEventPublisher;
    }

    public Mono<ContractorDTO> getContractor(String contractorId){
        Mono<Contractor> Contractor=this.contractorRepository.findContractorById(contractorId);
        return Contractor.map(ContractorMapper::toDTO);
    }

    public Flux<List<ContractorDTO>> getAllContractors(){

        Flux<List<Contractor>> Contractors = this.contractorRepository.findAllContractors();
        return Contractors.map(ContractorMapper::toDTOs);
    }

    public Mono<ContractorDTO> updateContractor(String contractorId, CreateContractorDTO createContractorDTO){
        // TODO: implement find + update semantics similar to customers/catalogs
        Contractor contractor=new Contractor();
        return Mono.just(ContractorMapper.toDTO(contractor));
    }

    public Mono<ContractorDTO> createContractor(CreateContractorDTO createContractorDTO){
        Contractor contractor= ContractorMapper.toModel(createContractorDTO);
        // Generate business contractorId and let the model wire pk/sk
        String newContractorId = idGeneratorService.createContractorId();
        contractor.setContractorId(newContractorId);
        contractor.setCreatedOn(Instant.now());
        contractor.setModifiedOn(Instant.now());

        Mono<Contractor> contractorResult = this.contractorRepository.saveContractor(contractor).thenReturn(contractor).doOnSuccess(x ->
                {
                    System.out.println("Publishing Contractor: " + x.getContractorId());
                    //Publish the Contractor event to listeners
                    //ContractorsEventPublisher.publishContractorsCreatedEvent(x.getContractorId(), x);
                }
        );


        System.out.println("Returning from createContractor service: " + contractor.getContractorId());
        return contractorResult.map(ContractorMapper::toDTO);
    }

    public Mono<Void> deleteContractor(String contractorId){
         return this.contractorRepository.deleteContractor(contractorId);
    }

    public Flux<ContractorDTO> findContractorsByZIPCode(String zipCode){
        return this.contractorRepository.findContractorsByZIPCode(zipCode).map(ContractorMapper::toDTO);
    }

}
