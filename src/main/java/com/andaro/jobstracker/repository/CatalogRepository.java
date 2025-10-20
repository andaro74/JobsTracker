package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.Catalog;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

public interface CatalogRepository {

    public Flux<List<Catalog>> findAllCatalogs();

    public Mono<Catalog> saveCatalog(Catalog catalog);

    public Mono<Catalog> findCatalogById(UUID id);

    public Mono<Void> deleteCatalog(UUID id);

}
