package com.andaro.jobstracker.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.Instant;
import java.util.UUID;

@DynamoDbBean
public class Customer {
    private UUID id;
    private String customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private Instant createdOn;
    private Instant modifiedOn;

    public UUID getId(){
        return id;
    }

    public void setId(UUID value){
        this.id=value;
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("customerId")
    public String getCustomerId() { return customerId; }

    public void setCustomerId(String value) {
        this.customerId= "CustomerNumber#" + value;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public void setFirstName(String value){
        this.firstName=value;
    }


    public String getLastName(){
        return lastName;
    }

    public void setLastName(String value){
        this.lastName=value;
    }

    public void setAddress(String value) { this.address=value; }

    public String getAddress(){ return this.address; }

    public String getCity(){
        return city;
    }

    public void setCity(String value){
        this.city=value;
    }

    public String getState(){
        return state;
    }

    public void setState(String value){
        this.state=value;
    }

    public void setZipCode(String value) { this.zipCode=value; }

    public String getZipCode() { return this.zipCode; }

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
