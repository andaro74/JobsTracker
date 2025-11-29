package com.andaro.jobstracker.controller;

import com.andaro.jobstracker.dto.CatalogDTO;
import com.andaro.jobstracker.dto.CreateCatalogDTO;
import com.andaro.jobstracker.dto.UpdateCatalogDTO;
import com.andaro.jobstracker.service.CatalogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
public class CatalogController {

    private final CatalogService service;

    public CatalogController(CatalogService catalogService){
        this.service = catalogService;
    }

    @GetMapping
    public Flux<CatalogDTO> getCatalogs(){
        return service.getAllCatalogs();
    }

    /**
     * Retrieve a catalog item by its string business identifier (catalogId),
     * e.g. "CAT-0001".
     */
    @GetMapping("/{catalogId}")
    public Mono<ResponseEntity<CatalogDTO>> getJob(@PathVariable String catalogId){
        return service.getCatalog(catalogId)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update a catalog item identified by its string business identifier (catalogId).
     */
    @PutMapping("/{catalogId}")
    public Mono<ResponseEntity<CatalogDTO>> updateJob(@PathVariable String catalogId, @Valid @RequestBody UpdateCatalogDTO item){
        if (item.priceRateType() == null) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        return service.updateCatalog(catalogId, item)
                .map(catalogDTO -> new ResponseEntity<>(catalogDTO, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));

    }

    @PostMapping
    public Mono<ResponseEntity<CatalogDTO>> createCatalog(@Valid @RequestBody CreateCatalogDTO item){
        if (item.priceRateType() == null) {
            return Mono.just(ResponseEntity.badRequest().build());
        }
        return service.createCatalog(item)
                .map(catalogDTO -> new ResponseEntity<>(catalogDTO, HttpStatus.CREATED));
    }

    /**
     * Delete a catalog item by its string business identifier (catalogId).
     */
    @DeleteMapping("/{catalogId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Void> deleteCatalog(@PathVariable String catalogId){
        return service.deleteCatalog(catalogId);
    }


}
