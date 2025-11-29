package com.andaro.jobstracker.model;

import java.util.Date;

public class JobItemRequest {

    private String jobDescription;
    private JobStatus jobStatus;
    private Date expectedCompletion;
    private Date actualCompletion;
    private Date createdOn;
    private Date modifiedOn;
    private Catalog catalog;

    public JobItemRequest(){}

    public String getJobDescription(){
        return jobDescription;
    }

    public void setJobDescription(String value){
        this.jobDescription=value;
    }

    public JobStatus getJobStatus(){
        return this.jobStatus;
    }

    public void setJobStatus(JobStatus value){
        this.jobStatus=value;
    }

    public Date getExpectedCompletion(){
        return this.expectedCompletion;
    }

    public void setExpectedCompletion(Date value){
        this.expectedCompletion=value;
    }

    public Date getActualCompletion(){
        return this.actualCompletion;
    }

    public void setActualCompletion(Date value){
        this.actualCompletion=value;
    }

    public Date getCreatedOn(){
        return this.createdOn;
    }

    public void setCreatedOn(Date value){
        this.createdOn=value;
    }

    public Date getModifiedOn(){
        return this.modifiedOn;
    }

    public void setModifiedOn(Date value){
        this.modifiedOn=value;
    }

    public Catalog getCatalog() { return catalog; }

    public void setCatalog(Catalog catalog) { this.catalog = catalog; }

}