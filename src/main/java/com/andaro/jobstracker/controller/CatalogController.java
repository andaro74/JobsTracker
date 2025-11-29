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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Mono<ResponseEntity<?>> getCatalog(@PathVariable String catalogId){
        List<String> missingFields = validateCatalogId(catalogId);
        if (!missingFields.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of(
                    "message", "Missing required fields",
                    "fields", missingFields
            )));
        }
        return service.getCatalog(catalogId)
                .map(jobItem -> new ResponseEntity<>(jobItem, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Update a catalog item identified by its string business identifier (catalogId).
     */
    @PutMapping("/{catalogId}")
    public Mono<ResponseEntity<?>> updateCatalog(@PathVariable String catalogId, @Valid @RequestBody UpdateCatalogDTO item){
        List<String> missingFields = new ArrayList<>(validateCatalogId(catalogId));
        if (item == null) {
            missingFields.add("requestBody");
        }
        if (!missingFields.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of(
                    "message", "Missing required fields",
                    "fields", missingFields
            )));
        }
        return service.updateCatalog(catalogId, item)
                .map(catalogDTO -> new ResponseEntity<>(catalogDTO, HttpStatus.OK))
                .defaultIfEmpty((new ResponseEntity<>(HttpStatus.NOT_FOUND)));

    }

    @PostMapping
    public Mono<ResponseEntity<?>> createCatalog(@Valid @RequestBody CreateCatalogDTO item){
        List<String> missingFields = validateCreateCatalog(item);
        if (!missingFields.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of(
                    "message", "Missing required fields",
                    "fields", missingFields
            )));
        }
        return service.createCatalog(item)
                .map(catalogDTO -> new ResponseEntity<>(catalogDTO, HttpStatus.CREATED));
    }

    /**
     * Delete a catalog item by its string business identifier (catalogId).
     */
    @DeleteMapping("/{catalogId}")
    public Mono<ResponseEntity<?>> deleteCatalog(@PathVariable String catalogId){
        List<String> missingFields = validateCatalogId(catalogId);
        if (!missingFields.isEmpty()) {
            return Mono.just(ResponseEntity.badRequest().body(Map.of(
                    "message", "Missing required fields",
                    "fields", missingFields
            )));
        }
        return service.deleteCatalog(catalogId)
                .then(Mono.just(ResponseEntity.ok().build()));
    }

    private List<String> validateCreateCatalog(CreateCatalogDTO item) {
        List<String> missing = new ArrayList<>();
        if (item == null) {
            missing.add("catalogName");
            missing.add("catalogDescription");
            missing.add("price");
            missing.add("tradeType");
            missing.add("priceRateType");
            return missing;
        }
        if (item.catalogName() == null || item.catalogName().isBlank()) {
            missing.add("catalogName");
        }
        if (item.catalogDescription() == null || item.catalogDescription().isBlank()) {
            missing.add("catalogDescription");
        }
        if (item.price() == null) {
            missing.add("price");
        }
        if (item.tradeType() == null) {
            missing.add("tradeType");
        }
        if (item.priceRateType() == null) {
            missing.add("priceRateType");
        }
        return missing;
    }

    private List<String> validateCatalogId(String catalogId) {
        List<String> missing = new ArrayList<>();
        if (catalogId == null || catalogId.isBlank()) {
            missing.add("catalogId");
        }
        return missing;
    }

}
