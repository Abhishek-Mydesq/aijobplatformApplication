package com.aijobplatform.job.service.impl;

import com.aijobplatform.job.dto.CreateJobRequest;
import com.aijobplatform.job.dto.JobResponse;
import com.aijobplatform.job.dto.PageResponse;
import com.aijobplatform.job.entity.Job;
import com.aijobplatform.job.exception.ResourceNotFoundException;
import com.aijobplatform.job.repository.JobRepository;
import com.aijobplatform.job.service.JobService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final ModelMapper modelMapper;
    @Override
    public JobResponse createJob(CreateJobRequest request) {
        Job job = modelMapper.map(request, Job.class);
        Job savedJob = jobRepository.save(job);
        return modelMapper.map(savedJob, JobResponse.class);
    }
    @Override
    public List<JobResponse> getAllJobs() {
        List<Job> jobs = jobRepository.findAll();
        return jobs.stream()
                .map(job -> modelMapper.map(job, JobResponse.class))
                .toList();
    }
    @Override
    public JobResponse getJobById(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id " + id));
        return modelMapper.map(job, JobResponse.class);
    }

    @Override
    public JobResponse updateJob(Long id, CreateJobRequest request) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id " + id));
        modelMapper.map(request, existingJob);
        Job updatedJob = jobRepository.save(existingJob);
        return modelMapper.map(updatedJob, JobResponse.class);
    }
    @Override
    public void deleteJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found with id " + id));
        jobRepository.delete(job);
    }
    @Override
    public PageResponse<JobResponse> searchJobs(String title, String location, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobPage = jobRepository
                .findByTitleContainingIgnoreCaseAndLocationContainingIgnoreCase(
                        title,
                        location,
                        pageable
                );
        List<JobResponse> jobs = jobPage.getContent()
                .stream()
                .map(job -> modelMapper.map(job, JobResponse.class))
                .toList();
        return new PageResponse<>(
                jobs,
                jobPage.getNumber(),
                jobPage.getSize(),
                jobPage.getTotalElements(),
                jobPage.getTotalPages(),
                jobPage.isLast()
        );
    }
}