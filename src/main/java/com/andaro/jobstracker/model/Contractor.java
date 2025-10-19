package com.andaro.jobstracker.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@DynamoDbBean
public class Contractor {
    private UUID id;
    private String contractorId;
    private String firstName;
    private String lastName;
    private String companyName;
    private String licenseNumber;
    private Instant createdOn;
    private Instant modifiedOn;

    public UUID getId(){
        return id;
    }

    public void setId(UUID value){
        this.id=value;
    }

    @DynamoDbPartitionKey
    public String getContractorId() { return contractorId; }

    public void setContractorId(String value) {
        this.contractorId= "ContractorNumber#" + value;
    }

    //@DynamoDbSortKey
    //public String getJobUpdatedDate() {return this.jobUpdatedDate;}

    //public void setJobUpdatedDate(String value) {
   //      this.jobUpdatedDate="JobUpdatedDate#" + value;
    //}

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

    public String getCompanyName(){
        return companyName;
    }

    public void setCompanyName(String value){
        this.companyName=value;
    }

    public String getLicenseNumber(){
        return licenseNumber;
    }

    public void setLicenseNumber(String value){
        this.licenseNumber=value;
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
