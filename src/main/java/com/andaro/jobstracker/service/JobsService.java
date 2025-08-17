package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.JobItemDTO;
import com.andaro.jobstracker.dto.JobItemRequestDTO;
import com.andaro.jobstracker.model.JobItem;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.UUID;

public interface JobsService {
    public Mono<JobItemDTO> GetJob(UUID id);
    public Flux<List<JobItemDTO>> GetAllJobs();

    public Mono<JobItemDTO> UpdateJob(UUID id, JobItemRequestDTO item);

    public Mono<JobItemDTO> CreateJob(JobItemRequestDTO item);

    public void DeleteJob(UUID id);
}
