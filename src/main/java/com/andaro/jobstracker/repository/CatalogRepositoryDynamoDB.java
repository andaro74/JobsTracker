package com.andaro.jobstracker.repository;

import com.andaro.jobstracker.model.Catalog;
import com.andaro.jobstracker.service.IdGeneratorService;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbAsyncTable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PagePublisher;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.time.Instant;

@Repository
public class CatalogRepositoryDynamoDB implements CatalogRepository {

    private final String CATALOG_KEY_PREFIX="CATALOG#";
    private final String TRADE_KEY_PREFIX ="TRADE#";

    private final DynamoDbAsyncTable<Catalog> catalogTable;
    static final TableSchema<Catalog> catalogTableSchema = TableSchema.fromBean(Catalog.class);
    private final IdGeneratorService idGeneratorService;

    public CatalogRepositoryDynamoDB(DynamoDbEnhancedAsyncClient dynamoDbEnhancedAsyncClient, IdGeneratorService idGeneratorService){
        String CATALOG_TABLE_NAME = "Catalog";
        this.catalogTable = dynamoDbEnhancedAsyncClient.table(CATALOG_TABLE_NAME, catalogTableSchema);
        this.idGeneratorService = idGeneratorService;
    }

    private String buildCatalogPk(String catalogId) {
        return DynamoKeyBuilder.buildPk(CATALOG_KEY_PREFIX, catalogId);
    }

    private String buildCatalogSk(String tradeType) {
        return DynamoKeyBuilder.buildSk(TRADE_KEY_PREFIX, tradeType);
    }

    public Flux<Catalog> findAllCatalogs(){
        return Flux.from(catalogTable.scan())
                .flatMapIterable(Page::items)
                .sort((a, b) -> {
                    String skuA = a.getSku() != null ? a.getSku() : "";
                    String skuB = b.getSku() != null ? b.getSku() : "";
                    return skuA.compareToIgnoreCase(skuB);
                });
    }

    public Mono<Catalog> saveCatalog(Catalog catalog) {
        // Only assign ID and partition key if this is a new catalog
        if (catalog.getCatalogId() == null) {
            String newCatalogId = idGeneratorService.createCatalogId();
            catalog.setCatalogId(newCatalogId);
            catalog.setPK(buildCatalogPk(newCatalogId));
            System.out.println("Catalog PK: " + catalog.getPK());
        }

        catalog.setSK(buildCatalogSk(catalog.getTradeType().toString()));
        System.out.println("Catalog SK: " + catalog.getSK());

        catalog.setModifiedOn(Instant.now());
        System.out.println("Saving Catalog: " + catalog);

        return Mono.fromFuture(catalogTable.putItem(catalog))
                .then(Mono.just(catalog))
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to put item: " + e.getMessage());
                });
    }

    public Mono<Catalog> findCatalogById(String catalogId){
        return findCatalogById(catalogId, TRADE_KEY_PREFIX);
    }

    public Mono<Catalog> findCatalogById(String catalogId, String sortKeyPartial){

        String catalogKey = buildCatalogPk(catalogId);
        String skPrefix = (sortKeyPartial != null && !sortKeyPartial.isBlank()) ? sortKeyPartial : null;
        System.out.println("Catalog PK lookup " + catalogKey + (skPrefix != null ? ", SK prefix " + skPrefix : ""));

        QueryConditional conditional = skPrefix != null
                ? QueryConditional.sortBeginsWith(
                        Key.builder()
                                .partitionValue(catalogKey)
                                .sortValue(skPrefix)
                                .build())
                : QueryConditional.keyEqualTo(
                        Key.builder()
                                .partitionValue(catalogKey)
                                .build());

        PagePublisher<Catalog> results = catalogTable.query(conditional);

        return Flux.from(results)
                .flatMapIterable(Page::items)
                .next()
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed to find Catalog by Id: " + e.getMessage());
                });

    }

    public Mono<Void> deleteCatalog(String catalogId){
        String catalogKey = buildCatalogPk(catalogId);

        return findCatalogById(catalogId, TRADE_KEY_PREFIX)
                .switchIfEmpty(Mono.error(new IllegalStateException("Catalog not found for id " + catalogId)))
                .flatMap(catalog -> {
                    String sortKey = catalog.getSK();
                    Key key = Key.builder()
                            .partitionValue(catalogKey)
                            .sortValue(sortKey)
                            .build();

                    return Mono.fromFuture(catalogTable.deleteItem(key))
                            .then()
                            .doOnSuccess(ignored -> System.out.println("Deleted Catalog with PK/SK: " + catalogKey + " / " + sortKey));
                })
                .doOnError(DynamoDbException.class, e -> {
                    System.err.println("Failed deleting Catalog with PK " + catalogKey + ": " + e.getMessage());
                });
    }
}
