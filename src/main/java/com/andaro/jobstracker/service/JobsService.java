package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CreateJobItemDTO;
import com.andaro.jobstracker.dto.JobItemDTO;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.UUID;

public interface JobsService {
    public Mono<JobItemDTO> GetJob(UUID id);
    public Flux<List<JobItemDTO>> GetAllJobs();

    public Mono<JobItemDTO> UpdateJob(UUID id, CreateJobItemDTO item);

    public Mono<JobItemDTO> CreateJob(CreateJobItemDTO item);

    public void DeleteJob(UUID id);
}
