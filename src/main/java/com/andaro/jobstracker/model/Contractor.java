package com.andaro.jobstracker.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;

@DynamoDbBean
public class Contractor {
    // Physical keys in DynamoDB
    private String pk;
    private String sk;
    // Business identifier
    private String contractorId;
    private String firstName;
    private String lastName;
    private TradeType tradeType;
    private String companyName;
    private String licenseNumber;
    private String zipCode;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String emailAddress;
    private String phoneNumber;
    private Instant createdOn;
    private Instant modifiedOn;

    // Partition key stored as attribute "pk"
    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPk() { return pk; }

    public void setPk(String pk) { this.pk = pk; }

    // Sort key stored as attribute "sk"
    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getSk() { return sk; }

    public void setSk(String sk) { this.sk = sk; }

    // Business contractor id; also drives pk
    @DynamoDbAttribute("contractorId")
    public String getContractorId() { return contractorId; }

    public void setContractorId(String value) {
        this.contractorId = value;
        this.pk = "ContractorNumber#" + value;
        if (this.sk == null) {
            this.sk = "CONTRACTOR";
        }
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

    public TradeType getTradeType() { return tradeType; }

    public void setTradeType(TradeType tradeType) { this.tradeType = tradeType; }

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

    public void setZipCode(String value) { this.zipCode=value; }

    public String getZipCode() { return this.zipCode; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getAddress2() { return address2; }

    public void setAddress2(String address2) { this.address2 = address2; }

    public String getCity() { return city; }

    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }

    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = CountryDefaults.defaultIfBlank(country); }

    public String getEmailAddress() { return emailAddress; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

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
