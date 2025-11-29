package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CreateJobItemDTO;
import com.andaro.jobstracker.dto.JobItemDTO;
import com.andaro.jobstracker.mapper.JobItemMapper;
import com.andaro.jobstracker.model.JobItem;
import com.andaro.jobstracker.model.JobStatus;
import com.andaro.jobstracker.repository.JobsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;

@Service
public class JobsServiceImpl implements JobsService {

    private final JobItemMapper jobItemMapper;
    private final JobsRepository jobsRepository;
    private final JobsEventPublisher jobsEventPublisher;

    public JobsServiceImpl(JobItemMapper jobItemMapper, JobsRepository jobsRepository, JobsEventPublisher jobsEventPublisher){
        this.jobItemMapper = jobItemMapper;
        this.jobsRepository = jobsRepository;
        this.jobsEventPublisher = jobsEventPublisher;
    }

    public Mono<JobItemDTO> GetJob(String jobId){
        Mono<JobItem> jobItem = this.jobsRepository.findJobById(jobId);
        return jobItem.map(jobItemMapper::toDTO);
    }

    public Flux<List<JobItemDTO>> GetAllJobs(){

        Flux<List<JobItem>> jobItems = this.jobsRepository.findAllJobs();
        return jobItems.map(jobItemMapper::toDTOs);
    }

    public Mono<JobItemDTO> UpdateJob(String jobId, CreateJobItemDTO item){
        return this.jobsRepository.findJobById(jobId)
                .flatMap(existing -> {
                    existing.setJobDescription(item.jobDescription());
                    existing.setExpectedCompletion(item.expectedCompletion());
                    if (item.catalogId() != null) {
                        existing.setCatalogId(item.catalogId());
                        existing.setJobCatalogId(item.catalogId());
                    }
                    if (item.customerId() != null) {
                        existing.setCustomerId(item.customerId());
                    }
                    if (item.contractorId() != null) {
                        existing.setContractorId(item.contractorId());
                    }
                    if (item.jobStatus() != null) {
                        existing.setJobStatus(JobStatus.valueOf(item.jobStatus().name()));
                    }
                    existing.setModifiedOn(Instant.now());
                    return this.jobsRepository.saveJob(existing);
                })
                .map(jobItemMapper::toDTO);
    }

    public Mono<JobItemDTO> CreateJob(CreateJobItemDTO item){
        JobItem jobItem = jobItemMapper.toModel(item);
        jobItem.setJobCatalogId(item.catalogId());
        jobItem.setCreatedOn(Instant.now());
        jobItem.setModifiedOn(Instant.now());
        jobItem.setJobStatus(item.jobStatus() != null ? JobStatus.valueOf(item.jobStatus().name()) : JobStatus.REQUESTED);
        Mono<JobItem> jobItemResult = this.jobsRepository.saveJob(jobItem).thenReturn(jobItem).doOnSuccess(x ->
                {
                    System.out.println("Publishing " + x);
                    //Publish the job event to listeners
                    jobsEventPublisher.publishJobsCreatedEvent(x.getJobId(), x);
                }
        );


        return jobItemResult.map(jobItemMapper::toDTO);
    }

    public void DeleteJob(String jobId){
         this.jobsRepository.deleteJob(jobId);
    }

}
