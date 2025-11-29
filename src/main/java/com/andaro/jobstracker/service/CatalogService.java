package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CreateCatalogDTO;
import com.andaro.jobstracker.dto.CatalogDTO;
import com.andaro.jobstracker.dto.UpdateCatalogDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CatalogService {

    Mono<CatalogDTO> getCatalog(String catalogId);

    Flux<CatalogDTO> getAllCatalogs();

    Mono<CatalogDTO> updateCatalog(String catalogId, UpdateCatalogDTO item);

    Mono<CatalogDTO> createCatalog(CreateCatalogDTO item);

    Mono<Void> deleteCatalog(String catalogId);
}
