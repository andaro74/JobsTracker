package com.andaro.jobstracker.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;

@DynamoDbBean
public class Customer {
    // Physical keys in DynamoDB
    private String pk;
    private String sk;
    // Logical business id
    private String customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String address2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String emailAddress;
    private String phoneNumber;
    private String companyName;
    private Instant createdOn;
    private Instant modifiedOn;

    // Partition key stored as attribute "pk"
    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPk() { return pk; }

    public void setPk(String pk) { this.pk = pk; }

    // Sort key stored as attribute "sk" (can be used for GSI or simple type marker)
    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getSk() { return sk; }

    public void setSk(String sk) { this.sk = sk; }

    // Logical customer id; also drives pk value with the existing prefix convention
    @DynamoDbAttribute("customerId")
    public String getCustomerId() { return customerId; }

    public void setCustomerId(String value) {
        this.customerId = value;
        this.pk = "CustomerNumber#" + value;
        // optional: keep a simple type marker in sk
        if (this.sk == null) {
            this.sk = "CUSTOMER";
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

    public void setAddress(String value) { this.address=value; }

    public String getAddress(){ return this.address; }

    public String getAddress2() { return address2; }

    public void setAddress2(String address2) { this.address2 = address2; }

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

    public String getCountry() { return country; }

    public void setCountry(String country) { this.country = CountryDefaults.defaultIfBlank(country); }

    public String getEmailAddress() { return emailAddress; }

    public void setEmailAddress(String emailAddress) { this.emailAddress = emailAddress; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getCompanyName() { return companyName; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

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
