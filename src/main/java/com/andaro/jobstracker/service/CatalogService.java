package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CreateCatalogDTO;
import com.andaro.jobstracker.dto.CatalogDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CatalogService {

    public Mono<CatalogDTO> getCatalog(UUID id);

    public Flux<List<CatalogDTO>> getAllCatalogs();

    public Mono<CatalogDTO> updateCatalog(UUID id, CreateCatalogDTO item);

    public Mono<CatalogDTO> createCatalog(CreateCatalogDTO item);

    public Mono<Void> deleteCatalog(UUID id);
}
