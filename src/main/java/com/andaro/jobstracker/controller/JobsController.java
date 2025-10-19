package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.CreateJobItemDTO;
import com.andaro.jobstracker.service.JobsService;
import com.andaro.jobstracker.dto.JobItemDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/jobs")
public class JobsController {

    private final JobsService service;

    public JobsController(JobsService jobsService){
        this.service = jobsService;
    }

    @GetMapping
    public Flux<List<JobItemDTO>> getJobs(){
        return service.GetAllJobs();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<JobItemDTO>> getJob(@PathVariable UUID id){
        return service.GetJob(id)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<JobItemDTO>> updateJob(@PathVariable UUID id, @RequestBody CreateJobItemDTO item){
        return service.UpdateJob(id, item)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));

    }

    @PostMapping
    public Mono<ResponseEntity<JobItemDTO>> createJob(@RequestBody CreateJobItemDTO item){
        return service.CreateJob(item)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.CREATED));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteJob(@PathVariable UUID id){
        service.DeleteJob(id);
    }


}

