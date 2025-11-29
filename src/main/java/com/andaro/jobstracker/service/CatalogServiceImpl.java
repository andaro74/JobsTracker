package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CatalogDTO;
import com.andaro.jobstracker.dto.CreateCatalogDTO;
import com.andaro.jobstracker.dto.UpdateCatalogDTO;
import com.andaro.jobstracker.mapper.CatalogMapper;
import com.andaro.jobstracker.model.Catalog;
import com.andaro.jobstracker.repository.CatalogRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CatalogMapper catalogMapper;
    private final CatalogRepository catalogRepository;

    public CatalogServiceImpl(
            CatalogMapper catalogMapper,
            CatalogRepository catalogRepository
    ){
        this.catalogMapper = catalogMapper;
        this.catalogRepository = catalogRepository;
    }

    @Override
    public Mono<CatalogDTO> getCatalog(String catalogId){
        return this.catalogRepository.findCatalogById(catalogId).map(catalogMapper::toDTO);
    }

    @Override
    public Flux<CatalogDTO> getAllCatalogs(){
        return this.catalogRepository.findAllCatalogs()
                .map(catalogMapper::stripKeys);
    }

    @Override
    public Mono<CatalogDTO> updateCatalog(String catalogId, UpdateCatalogDTO updateCatalogDTO){
        return this.catalogRepository.findCatalogById(catalogId)
                .flatMap(existing -> {
                    if (updateCatalogDTO.catalogDescription() != null && !updateCatalogDTO.catalogDescription().isBlank()) {
                        existing.setCatalogDescription(updateCatalogDTO.catalogDescription());
                    }
                    if (updateCatalogDTO.price() != null) {
                        existing.setPrice(updateCatalogDTO.price());
                    }
                    if (updateCatalogDTO.tradeType() != null) {
                        existing.setTradeType(catalogMapper.toModel(updateCatalogDTO.tradeType()));
                    }
                    if (updateCatalogDTO.priceRateType() != null) {
                        existing.setPriceRateType(catalogMapper.toModel(updateCatalogDTO.priceRateType()));
                    }
                    existing.setModifiedOn(Instant.now());
                    return this.catalogRepository.saveCatalog(existing);
                })
                .map(catalogMapper::toDTO);
    }

    @Override
    public Mono<CatalogDTO> createCatalog(CreateCatalogDTO createCatalogDTO){
        Catalog catalog= catalogMapper.toModel(createCatalogDTO);
        catalog.setCatalogName(createCatalogDTO.catalogName());
        catalog.setCatalogDescription(createCatalogDTO.catalogDescription());
        catalog.setPrice(createCatalogDTO.price());
        catalog.setCreatedOn(Instant.now());
        catalog.setModifiedOn(Instant.now());
        catalog.setSku(computeSku(createCatalogDTO.catalogName()));
        catalog.setTradeType(catalogMapper.toModel(createCatalogDTO.tradeType()));
        catalog.setPriceRateType(catalogMapper.toModel(createCatalogDTO.priceRateType()));

        Mono<Catalog> catalogResult = this.catalogRepository.saveCatalog(catalog).thenReturn(catalog).doOnSuccess(x ->
                {
                    System.out.println("Publishing Catalog: " + x.getCatalogId());
                }
        );

        System.out.println("Returning from createCatalog service: " + catalog.getCatalogId());
        return catalogResult.map(catalogMapper::toDTO);
    }

    @Override
    public Mono<Void> deleteCatalog(String catalogId){
         return this.catalogRepository.deleteCatalog(catalogId);
    }

    private String computeSku(String name) {
        if (name == null) return null;
        String base = name.trim().toUpperCase().replaceAll("[^A-Z0-9]+", "-");
        base = base.replaceAll("^-+|-+$", "");
        return base;
    }

}
