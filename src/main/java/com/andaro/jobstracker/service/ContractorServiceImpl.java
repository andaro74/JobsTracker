package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.ContractorResponse;
import com.andaro.jobstracker.dto.ContractorRequest;
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

    private final ContractorMapper contractorMapper;
    private final ContractorRepository contractorRepository;
    private final IdGeneratorService idGeneratorService;

    public ContractorServiceImpl(
            ContractorMapper contractorMapper,
            ContractorRepository contractorRepository,
            IdGeneratorService idGeneratorService){
        this.contractorMapper = contractorMapper;
        this.contractorRepository = contractorRepository;
        this.idGeneratorService = idGeneratorService;
    }

    public Mono<ContractorResponse> getContractor(String contractorId){
        Mono<Contractor> contractor = this.contractorRepository.findContractorById(contractorId);
        return contractor.map(contractorMapper::toDTO);
    }

    public Flux<ContractorResponse> getAllContractors(){
        return this.contractorRepository.findAllContractors().map(contractorMapper::toDTO);
    }

    public Mono<ContractorResponse> updateContractor(String contractorId, ContractorRequest contractorRequest){
        return contractorRepository.findContractorById(contractorId)
                .flatMap(existing -> {
                    existing.setFirstName(contractorRequest.firstName());
                    existing.setLastName(contractorRequest.lastName());
                    existing.setCompanyName(contractorRequest.companyName());
                    existing.setTradeType(contractorMapper.toModel(contractorRequest.tradeType()));
                    existing.setLicenseNumber(contractorRequest.licenseNumber());
                    existing.setZipCode(contractorRequest.zipCode());
                    existing.setAddress(contractorRequest.address());
                    existing.setAddress2(contractorRequest.address2());
                    existing.setCity(contractorRequest.city());
                    existing.setState(contractorRequest.state());
                    existing.setCountry(contractorRequest.country());
                    existing.setEmailAddress(contractorRequest.emailAddress());
                    existing.setPhoneNumber(contractorRequest.phoneNumber());
                    existing.setModifiedOn(Instant.now());
                    return contractorRepository.saveContractor(existing)
                            .thenReturn(contractorMapper.toDTO(existing));
                });
     }

    public Mono<ContractorResponse> createContractor(ContractorRequest contractorRequest){
        Contractor contractor = contractorMapper.toModel(contractorRequest);
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
        return contractorResult.map(contractorMapper::toDTO);
    }

    public Mono<Void> deleteContractor(String contractorId){
         return this.contractorRepository.deleteContractor(contractorId);
    }

    public Flux<ContractorResponse> findContractorsByZIPCode(String zipCode){
        return this.contractorRepository.findContractorsByZIPCode(zipCode).map(contractorMapper::toDTO);
    }

}
