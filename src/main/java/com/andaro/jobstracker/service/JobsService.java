package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CreateJobItemDTO;
import com.andaro.jobstracker.dto.JobItemDTO;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;

public interface JobsService {
    public Mono<JobItemDTO> GetJob(String jobId);
    public Flux<List<JobItemDTO>> GetAllJobs();

    public Mono<JobItemDTO> UpdateJob(String jobId, CreateJobItemDTO item);

    public Mono<JobItemDTO> CreateJob(CreateJobItemDTO item);

    public void DeleteJob(String jobId);
}
