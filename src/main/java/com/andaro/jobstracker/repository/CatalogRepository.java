package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.Catalog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CatalogRepository {

    Flux<Catalog> findAllCatalogs();

    Mono<Catalog> saveCatalog(Catalog catalog);

    Mono<Catalog> findCatalogById(String catalogId);

    Mono<Catalog> findCatalogById(String catalogId, String sortKeyPartial);

    Mono<Void> deleteCatalog(String catalogId);

}
