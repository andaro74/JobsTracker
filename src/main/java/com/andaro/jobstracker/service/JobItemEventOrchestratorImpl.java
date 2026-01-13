package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.ContractorResponse;
import com.andaro.jobstracker.events.JobItemCreateEvent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@Service
public class JobItemEventOrchestratorImpl implements JobItemEventOrchestrator{

    private final ContractorService contractorService;
    private final JobsService jobsService;
    private final CustomerService customerService;

    public JobItemEventOrchestratorImpl(
            ContractorService contractorService,
            JobsService jobsService,
            CustomerService customerService){
        this.contractorService=contractorService;
        this.jobsService=jobsService;
        this.customerService=customerService;
    }

    @Transactional
    public void processNewJobItem(JobItemCreateEvent event) {
        System.out.println("Processing New Job Item: " + event);

        ArrayList<ContractorResponse> contractorList = new ArrayList<>();
         Flux<ContractorResponse> contractorFlux = contractorService
                 .findContractorsByZIPCode("90405");

         contractorFlux.subscribe(contractorList::add,
                 error -> System.err.println("Error: " + error.getMessage()));


    }



}
