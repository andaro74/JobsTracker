package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.JobItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface JobsRepository {

    Flux<List<JobItem>> findAllJobs();

    Mono<JobItem> saveJob(JobItem item);

    Mono<JobItem> findJobById(String jobId);

    void deleteJob(String jobId);

}
