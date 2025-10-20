package com.andaro.jobstracker.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
public class Catalog {

    private UUID id;
    private String catalogId;
    private String catalogName;
    private String catalogDescription;
    private Double hourlyRate;
    private Instant createdOn;
    private Instant modifiedOn;

    public UUID getId(){
        return id;
    }

    public void setId(UUID value){
        this.id=value;
    }

    @DynamoDbPartitionKey
    public String getCatalogId() { return catalogId; }

    public void setCatalogId(String value) {
        this.catalogId= "CatalogNumber#" + value;
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

    public Double getHourlyRate(){
        return this.hourlyRate;
    }

    public void setHourlyRate(Double value){
        this.hourlyRate =value;
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
