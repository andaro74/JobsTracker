package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.JobItemDTO;
import com.andaro.jobstracker.dto.JobItemRequestDTO;
import com.andaro.jobstracker.mapper.JobItemMapper;
import com.andaro.jobstracker.model.JobItem;
import com.andaro.jobstracker.repository.JobsRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class JobsServiceImpl implements JobsService {

    private final JobItemMapper jobItemMapper;
    private final JobsRepository jobsRepository;

    public JobsServiceImpl(JobItemMapper jobItemMapper, JobsRepository jobsRepository){
        this.jobItemMapper=jobItemMapper;
        this.jobsRepository=jobsRepository;
    }

    public Mono<JobItemDTO> GetJob(UUID id){
        JobItem jobItem = new JobItem(id,"Job" + id);
        return Mono.justOrEmpty(jobItemMapper.toDTO(jobItem));

    }

    public Flux<List<JobItemDTO>> GetAllJobs(){

        List<JobItem> jobItems = this.jobsRepository.getAllJobs();
        return Flux.just(jobItemMapper.toDTOs(jobItems));

    }

    public Mono<JobItemDTO> UpdateJob(UUID id, JobItemRequestDTO item){
        JobItem jobItem=new JobItem(id, "Job" + id);


        return Mono.just(jobItemMapper.toDTO(jobItem));
    }

    public Mono<JobItemDTO> CreateJob(JobItemRequestDTO item){
        JobItem jobItem=new JobItem(UUID.randomUUID(), "Job" + UUID.randomUUID());



        return Mono.just(jobItemMapper.toDTO(jobItem));
    }

    public void DeleteJob(UUID id){

    }

}
