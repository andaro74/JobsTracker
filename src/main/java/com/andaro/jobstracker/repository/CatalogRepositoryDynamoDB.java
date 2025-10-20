package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Catalog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Repository
public class CatalogRepositoryDynamoDB implements CatalogRepository {

    private static final Logger log = LoggerFactory.getLogger(CatalogRepositoryDynamoDB.class);
    private final String CATALOG_KEY_PREFIX="CatalogNumber#";

    private final DynamoDbAsyncTable<Catalog> catalogTable;
    static final TableSchema<Catalog> catalogTableSchema = TableSchema.fromBean(Catalog.class);

    public CatalogRepositoryDynamoDB(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient){
        String CATALOG_TABLE_NAME = "Catalog";
        this.catalogTable = dynamoDbEnhancedAsyncClient.table(CATALOG_TABLE_NAME, catalogTableSchema);
    }

    public Flux<List<Catalog>> findAllCatalogs(){
        List<Catalog> catalogs = new ArrayList<Catalog>();

        PagePublisher<Catalog> results = catalogTable.scan();
        results.subscribe(x-> x
                .items().stream()
                .forEach(item-> {
                        System.out.println(item.getCatalogId());
                        Catalog catalog=new Catalog();
                        catalog.setId(item.getId());
                        catalog.setCatalogName(item.getCatalogName());
                        catalog.setCatalogDescription(item.getCatalogDescription());
                        catalog.setHourlyRate(item.getHourlyRate());
                        catalog.setCreatedOn(item.getCreatedOn());
                        catalog.setModifiedOn(item.getModifiedOn());

                        catalogs.add(catalog);
                        }
                )
                )
                .join();
        return Flux.just(catalogs);
    }

    public Mono<Catalog> saveCatalog(Catalog catalog) {
        catalog.setId(UUID.randomUUID());
        catalog.setCatalogId(catalog.getId().toString());
        catalog.setModifiedOn(Instant.now());
        System.out.println("Saving Catalog: " + catalog);

        return Mono.fromFuture(catalogTable.putItem(catalog))
                .then(Mono.just(catalog))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to put item: " + e.getMessage());
                });
    }

    public Mono<Catalog> findCatalogById(UUID id){

        String catalogKey= CATALOG_KEY_PREFIX + id.toString();
        System.out.println("CatalogKey is " + catalogKey);
        System.out.println("Find Catalog By Id: " + id.toString());
        CompletableFuture<Catalog> future = this.catalogTable.getItem(Key.builder().partitionValue(catalogKey).build());

        return Mono.fromFuture(future)
                .then(Mono.justOrEmpty(future.join()))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to find Catalog by Id: " + e.getMessage());
                });

    }

    public Mono<Void> deleteCatalog(UUID id){
        String catalogKey= CATALOG_KEY_PREFIX + id.toString();
        CompletableFuture<Catalog> future = this.catalogTable.deleteItem(Key.builder().partitionValue(catalogKey).build());
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Completed Deleting Catalog");
            }
            else {
                System.out.println("Failed Deleting Catalog");
            }
        });
        return  Mono.empty();
    }
}
