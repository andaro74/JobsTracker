package com.andaro.jobstracker.service;

import com.andaro.jobstracker.dto.CatalogDTO;
import com.andaro.jobstracker.dto.CreateCatalogDTO;
import com.andaro.jobstracker.mapper.CatalogMapper;
import com.andaro.jobstracker.model.Catalog;
import com.andaro.jobstracker.repository.CatalogRepository;
import com.andaro.jobstracker.service.CatalogService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CatalogServiceImpl implements CatalogService {

    private final CatalogMapper catalogMapper;
    private final CatalogRepository catalogRepository;
    //private final CatalogEventPublisher catalogEventPublisher;

    public CatalogServiceImpl(
            CatalogMapper catalogMapper,
            CatalogRepository catalogRepository //,
            //CatalogEventPublisher catalogEventPublisher){
    ){
        this.catalogMapper = catalogMapper;
        this.catalogRepository = catalogRepository;
        //this.catalogEventPublisher = catalogEventPublisher;
    }

    public Mono<CatalogDTO> getCatalog(UUID id){
        return this.catalogRepository.findCatalogById(id).map(catalogMapper::toDTO);
    }

    public Flux<List<CatalogDTO>> getAllCatalogs(){
        return this.catalogRepository.findAllCatalogs().map(catalogMapper::toDTOs);
    }

    public Mono<CatalogDTO> updateCatalog(UUID id, CreateCatalogDTO createCatalogDTO){
        Catalog catalog=new Catalog();
        return Mono.just(catalogMapper.toDTO(catalog));
    }

    public Mono<CatalogDTO> createCatalog(CreateCatalogDTO createCatalogDTO){
        Catalog catalog= catalogMapper.toModel(createCatalogDTO);
        catalog.setId(UUID.randomUUID());
        catalog.setCreatedOn(Instant.now());
        catalog.setModifiedOn(Instant.now());

        Mono<Catalog> catalogResult = this.catalogRepository.saveCatalog(catalog).thenReturn(catalog).doOnSuccess(x ->
                {
                    System.out.println("Publishing Catalog: " + x.getCatalogId());
                    //Publish the Catalog event to listeners
                    //CatalogsEventPublisher.publishCatalogsCreatedEvent(x.getId().toString(), x);
                }
        );


        System.out.println("Returning from createCatalog service: " + catalog.getCatalogId());
        return catalogResult.map(catalogMapper::toDTO);
    }

    public Mono<Void> deleteCatalog(UUID id){
         return this.catalogRepository.deleteCatalog(id);
    }

}
