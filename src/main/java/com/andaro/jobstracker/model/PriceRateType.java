package com.andaro.jobstracker.model;

public enum PriceRateType {
    HOURLY("Hourly"),
    PER_JOB("Per Job"),
    DAILY("Daily"),
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly"),
    PER_VISIT("Per Visit"),
    PER_UNIT("Per Unit");

    private final String label;

    PriceRateType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

