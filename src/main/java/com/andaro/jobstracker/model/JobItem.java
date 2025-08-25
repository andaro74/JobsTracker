package com.andaro.jobstracker.model;

import java.util.Optional;
import java.util.UUID;

public class JobItem {

    final private UUID id;
    final private String jobName;

    public JobItem(UUID id, String jobName){
        this.id=id;
        this.jobName=jobName;
    }

    public UUID getId() {
        return id;
    }

    public String getJobName(){
        return jobName;
    }



}
