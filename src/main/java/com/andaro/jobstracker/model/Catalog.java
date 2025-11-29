package com.andaro.jobstracker.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
public class Catalog {
    private String catalogId;
    private String pk;
    private String sk;
    private String catalogName;
    private String catalogDescription;
    private Double price;
    private String sku;
    private TradeType tradeType;
    private PriceRateType priceRateType;
    private Instant createdOn;
    private Instant modifiedOn;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPK() {
        return pk;
    }

    public void setPK(String value) {
        this.pk = value;
    }

    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getSK() {
        return sk;
    }

    public void setSK(String value) {
        this.sk = value;
    }

    public String getCatalogId() { return catalogId; }

    public void setCatalogId(String value) {
        this.catalogId= value;
    }

    public String getCatalogName(){
        return catalogName;
    }

    public void setCatalogName(String value){
        this.catalogName=value;
    }

    public String getCatalogDescription(){
        return catalogDescription;
    }

    public void setCatalogDescription(String value){
        this.catalogDescription=value;
    }

    public Double getPrice(){
        return this.price;
    }

    public void setPrice(Double value){
        this.price = value;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public TradeType getTradeType() { return tradeType; }

    public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }

    public PriceRateType getPriceRateType() {
        return priceRateType != null ? priceRateType : PriceRateType.PER_JOB;
    }

    public void setPriceRateType(PriceRateType priceRateType) {
        this.priceRateType = priceRateType;
    }

    public Instant getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(Instant value) {
        this.createdOn = value;
    }

    public Instant getModifiedOn() {
        return this.modifiedOn;
    }

    public void setModifiedOn(Instant value) {
        this.modifiedOn = value;
    }


}
