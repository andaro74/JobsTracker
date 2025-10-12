package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CreateJobItemDTO;
import com.andaro.jobstracker.dto.JobItemDTO;
import com.andaro.jobstracker.mapper.JobItemMapper;
import com.andaro.jobstracker.model.JobItem;
import com.andaro.jobstracker.repository.JobsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

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

    public Mono<JobItemDTO> GetJob(UUID id){
        Mono<JobItem> jobItem=this.jobsRepository.findJobById(id);
        return jobItem.map(jobItemMapper::toDTO);
    }

    public Flux<List<JobItemDTO>> GetAllJobs(){

        Flux<List<JobItem>> jobItems = this.jobsRepository.findAllJobs();
        return jobItems.map(jobItemMapper::toDTOs);
    }

    public Mono<JobItemDTO> UpdateJob(UUID id, CreateJobItemDTO item){
        JobItem jobItem=new JobItem();
        return Mono.just(jobItemMapper.toDTO(jobItem));
    }

    public Mono<JobItemDTO> CreateJob(CreateJobItemDTO item){
        JobItem jobItem= jobItemMapper.toModel(item);
        jobItem.setId(UUID.randomUUID());
        jobItem.setCreatedOn(Instant.now());
        jobItem.setModifiedOn(Instant.now());
        jobItem.setJobStatus("Requested");
        Mono<JobItem> jobItemResult = this.jobsRepository.saveJob(jobItem).thenReturn(jobItem).doOnSuccess(x ->
                {
                    System.out.println("Publishing " + x);
                    //Publish the job event to listeners
                    jobsEventPublisher.publishJobsCreatedEvent(x.getId().toString(), x);
                }
        );


        return jobItemResult.map(jobItemMapper::toDTO);
    }

    public void DeleteJob(UUID id){
         this.jobsRepository.deleteJob(id);
    }

}
