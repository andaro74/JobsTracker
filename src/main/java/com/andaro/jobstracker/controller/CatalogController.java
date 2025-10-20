package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.CatalogDTO;
import com.andaro.jobstracker.dto.CreateCatalogDTO;
import com.andaro.jobstracker.dto.CreateJobItemDTO;
import com.andaro.jobstracker.dto.JobItemDTO;
import com.andaro.jobstracker.service.CatalogService;
import com.andaro.jobstracker.service.JobsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService service;

    public CatalogController(CatalogService catalogService){
        this.service = catalogService;
    }

    @GetMapping
    public Flux<List<CatalogDTO>> getCatalogs(){
        return service.getAllCatalogs();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CatalogDTO>> getJob(@PathVariable UUID id){
        return service.getCatalog(id)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CatalogDTO>> updateJob(@PathVariable UUID id, @RequestBody CreateCatalogDTO item){
        return service.updateCatalog(id, item)
                .map(catalogDTO -> new ResponseEntity<>(catalogDTO, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));

    }

    @PostMapping
    public Mono<ResponseEntity<CatalogDTO>> createCatalog(@RequestBody CreateCatalogDTO item){
        return service.createCatalog(item)
                .map(catalogDTO -> new ResponseEntity<>(catalogDTO, HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteCatalog(@PathVariable UUID id){
        return service.deleteCatalog(id);
    }


}

