package com.andaro.jobstracker.model;

import java.util.Date;

public class JobItemRequest {

    private  String jobName;
    private  String jobDescription;
    private  String assignedTo;
    private  String customerName;
    private  String jobStatus;
    private  Date expectedCompletion;
    private  Date actualCompletion;
    private  Date createdOn;
    private  Date modifiedOn;

    public JobItemRequest(){}

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

}