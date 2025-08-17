package com.andaro.jobstracker.model;

import java.util.UUID;

public class JobItem {

    final private UUID id;

    public JobItem(UUID id){
        this.id=id;
    }

    public UUID getId() {
        return id;
    }



}
