package com.andaro.jobstracker.controller;


import com.andaro.jobstracker.dto.JobItemRequestDTO;
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
@RequestMapping("/jobs")
public class JobsController {

    private final JobsService service;


    public JobsController(JobsService jobsService){
        this.service = jobsService;
    }

    @GetMapping
    public Flux<List<JobItemDTO>> GetJobs(){
        return service.GetAllJobs();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<JobItemDTO>> GetJob(@PathVariable UUID id){
        return service.GetJob(id)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<JobItemDTO>> UpdateJob(@PathVariable UUID id, @RequestBody JobItemRequestDTO item){
        return service.UpdateJob(id, item)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));

    }

    @PostMapping
    public Mono<ResponseEntity<JobItemDTO>> CreateJob(@RequestBody JobItemRequestDTO item){
        return service.CreateJob(item)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.CREATED));
    }


    @DeleteMapping("/{id}")
    public void DeleteJob(@PathVariable UUID id){
        service.DeleteJob(id);
    }


}

