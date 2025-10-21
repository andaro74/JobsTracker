package com.andaro.jobstracker.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@DynamoDbBean
public class JobItem {
    private UUID id;
    private UUID catalogId;
    private UUID customerId;
    private String jobId;
    private String jobName;
    private String jobDescription;
    private String assignedTo;
    private String customerName;
    private String jobStatus;
    private Instant expectedCompletion;
    private Instant actualCompletion;
    private Instant createdOn;
    private Instant modifiedOn;
    private String jobUpdatedDate;

    public UUID getId(){
        return id;
    }

    public void setId(UUID value){
        this.id=value;
    }

    @DynamoDbPartitionKey
    public String getJobId() { return jobId; }

    public void setJobId(String value) {
        this.jobId= "JobNumber#" + value;
    }

    //@DynamoDbSortKey
    public String getJobUpdatedDate() {return this.jobUpdatedDate;}

    public void setJobUpdatedDate(String value) {
        this.jobUpdatedDate="JobUpdatedDate#" + value;
    }

    public String getJobName(){
        return jobName;
    }

    public void setJobName(String value){
        this.jobName=value;
    }


    public String getAssignedTo(){
        return assignedTo;
    }

    public void setAssignedTo(String value){
        this.assignedTo=value;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String value){
        this.customerName=value;
    }

    public String getJobDescription(){
        return jobDescription;
    }

    public void setJobDescription(String value){
        this.jobDescription=value;
    }

    public String getJobStatus(){
        return this.jobStatus;
    }

    public void setJobStatus(String value){
        this.jobStatus=value;
    }


    public Instant getExpectedCompletion() {
        return this.expectedCompletion;
    }

    public void setExpectedCompletion(Instant value) {
        this.expectedCompletion = value;
    }

    public Instant getActualCompletion() {
        return this.actualCompletion;
    }

    public void setActualCompletion(Instant value) {
        this.actualCompletion = value;
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
