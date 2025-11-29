package com.andaro.jobstracker.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

import java.time.Instant;

@DynamoDbBean
public class JobItem {
    private String catalogId;
    private String customerId;
    private String contractorId;
    // New physical keys for DynamoDB
    private String pk;
    private String sk;
    // Logical identifiers
    private String jobId;
    private String jobCatalogId;
    private String jobDescription;
    private JobStatus jobStatus;
    private Instant expectedCompletion;
    private Instant actualCompletion;
    private Instant createdOn;
    private Instant modifiedOn;
    private String jobUpdatedDate;

    public String getCatalogId() { return catalogId; }
    public void setCatalogId(String catalogId) { this.catalogId = catalogId; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getContractorId() { return contractorId; }
    public void setContractorId(String contractorId) { this.contractorId = contractorId; }

    // Physical PK stored in DynamoDB
    @DynamoDbPartitionKey
    @DynamoDbAttribute("pk")
    public String getPk() { return pk; }
    public void setPk(String pk) { this.pk = pk; }

    // Physical SK stored in DynamoDB
    @DynamoDbSortKey
    @DynamoDbAttribute("sk")
    public String getSk() { return sk; }
    public void setSk(String sk) { this.sk = sk; }

    // Logical jobId that maps to pk using existing prefix convention
    public String getJobId() { return jobId; }
    public void setJobId(String value) {
        this.jobId = value;
        this.pk = "JobNumber#" + value;
    }

    // Logical catalog id for the job that maps to sk using existing prefix convention
    public String getJobCatalogId() { return jobCatalogId; }
    public void setJobCatalogId(String value) {
        this.jobCatalogId = value;
        this.sk = "CatalogNumber#" + value;
    }

    public String getJobUpdatedDate() {return this.jobUpdatedDate;}
    public void setJobUpdatedDate(String value) {
        this.jobUpdatedDate = "JobUpdatedDate#" + value;
    }

    public String getJobDescription(){ return jobDescription; }
    public void setJobDescription(String value){ this.jobDescription = value; }

    public JobStatus getJobStatus(){ return this.jobStatus; }
    public void setJobStatus(JobStatus value){ this.jobStatus = value; }

    public Instant getExpectedCompletion() { return this.expectedCompletion; }
    public void setExpectedCompletion(Instant value) { this.expectedCompletion = value; }

    public Instant getActualCompletion() { return this.actualCompletion; }
    public void setActualCompletion(Instant value) { this.actualCompletion = value; }

    public Instant getCreatedOn() { return this.createdOn; }
    public void setCreatedOn(Instant value) { this.createdOn = value; }

    public Instant getModifiedOn() { return this.modifiedOn; }
    public void setModifiedOn(Instant value) { this.modifiedOn = value; }
}
