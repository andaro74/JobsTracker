package com.andaro.jobstracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.andaro.jobstracker"})
public class JobsTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobsTrackerApplication.class, args);
    }

}
