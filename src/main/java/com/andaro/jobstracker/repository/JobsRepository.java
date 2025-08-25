package com.andaro.jobstracker.repository;
import com.andaro.jobstracker.model.JobItem;
import java.util.List;
import java.util.UUID;

public interface JobsRepository {

    public List<JobItem> getAllJobs();

    public JobItem createJob(JobItem item);

    public JobItem getJobById(UUID id);

    public JobItem updateJob(UUID id, JobItem item);

    public void deleteJob(UUID id);




}
