package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.JobItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

import java.util.List;
import java.util.UUID;

public interface JobsRepository {

    public Flux<List<JobItem>> findAllJobs();

    public Mono<PutItemResponse> saveJob(JobItem item);

    public Mono<JobItem> findJobById(UUID id);

    public void deleteJob(UUID id);




}
