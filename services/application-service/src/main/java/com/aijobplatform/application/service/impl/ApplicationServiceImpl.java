package com.aijobplatform.application.service.impl;

import com.aijobplatform.application.client.JobServiceClient;
import com.aijobplatform.application.client.ResumeServiceClient;
import com.aijobplatform.application.client.UserServiceClient;
import com.aijobplatform.application.common.ApiResponse;
import com.aijobplatform.application.dto.*;
import com.aijobplatform.application.entity.Application;
import com.aijobplatform.application.exception.ResourceNotFoundException;
import com.aijobplatform.application.repository.ApplicationRepository;
import com.aijobplatform.application.service.ApplicationService;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    private final UserServiceClient userServiceClient;
    private final JobServiceClient jobServiceClient;
    private final ResumeServiceClient resumeServiceClient;

    @Override
    public ApplicationResponse applyJob(ApplyJobRequest request) {

        // ✅ USER VALIDATION
        ApiResponse<UserResponse> userResponse =
                userServiceClient.getUserById(request.getUserId());

        if (userResponse == null ||
                !userResponse.isSuccess() ||
                userResponse.getData() == null) {

            throw new RuntimeException(
                    "User not found: " + request.getUserId());
        }
        ApiResponse<JobResponse> jobResponse =
                jobServiceClient.getJobById(request.getJobId());

        if (jobResponse == null ||
                !jobResponse.isSuccess() ||
                jobResponse.getData() == null) {

            throw new RuntimeException(
                    "Job not found: " + request.getJobId());
        }

        ApiResponse<ResumeResponse> resumeResponse =
                resumeServiceClient.getResumeById(request.getResumeId());

        if (resumeResponse == null ||
                !resumeResponse.isSuccess() ||
                resumeResponse.getData() == null) {

            throw new RuntimeException(
                    "Resume not found: " + request.getResumeId());
        }

        if (applicationRepository.existsByUserIdAndJobId(
                request.getUserId(),
                request.getJobId())) {

            throw new RuntimeException(
                    "User already applied to this job");
        }

        Application application = Application.builder()
                .userId(request.getUserId())
                .jobId(request.getJobId())
                .resumeId(request.getResumeId())
                .status(ApplicationStatus.APPLIED)
                .build();

        Application saved =
                applicationRepository.save(application);

        return modelMapper.map(saved, ApplicationResponse.class);
    }


    @Override
    public PageResponse<ApplicationResponse> getApplicationsByUser(
            Long userId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Application> applicationPage =
                applicationRepository.findByUserId(userId, pageable);

        List<ApplicationResponse> responses =
                applicationPage.getContent()
                        .stream()
                        .map(app ->
                                modelMapper.map(
                                        app,
                                        ApplicationResponse.class
                                )
                        )
                        .toList();

        return new PageResponse<>(
                responses,
                applicationPage.getNumber(),
                applicationPage.getSize(),
                applicationPage.getTotalElements(),
                applicationPage.getTotalPages(),
                applicationPage.isLast()
        );
    }


    @Override
    public PageResponse<ApplicationResponse> getApplicationsByJob(
            Long jobId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Application> applicationPage =
                applicationRepository.findByJobId(jobId, pageable);

        List<ApplicationResponse> responses =
                applicationPage.getContent()
                        .stream()
                        .map(app ->
                                modelMapper.map(
                                        app,
                                        ApplicationResponse.class
                                )
                        )
                        .toList();

        return new PageResponse<>(
                responses,
                applicationPage.getNumber(),
                applicationPage.getSize(),
                applicationPage.getTotalElements(),
                applicationPage.getTotalPages(),
                applicationPage.isLast()
        );
    }


    @Override
    public ApplicationResponse updateApplicationStatus(
            Long id,
            ApplicationStatus status
    ) {

        Application application =
                applicationRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Application not found"
                                ));

        application.setStatus(status);

        Application updated =
                applicationRepository.save(application);

        return modelMapper.map(
                updated,
                ApplicationResponse.class
        );
    }
    @Override
    public ApplicationResponse getApplicationById(Long id) {

        Application app = applicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found"
                        ));

        return modelMapper.map(app, ApplicationResponse.class);
    }


    @Override
    public long countByUser(Long userId) {
        return applicationRepository.countByUserId(userId);
    }


    @Override
    public long countByJob(Long jobId) {
        return applicationRepository.countByJobId(jobId);
    }


    @Override
    public void withdrawApplication(Long id) {

        Application app = applicationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application not found"
                        ));

        app.setStatus(ApplicationStatus.WITHDRAWN);

        applicationRepository.save(app);
    }


    @Override
    public List<ApplicationResponse> getByUserAndStatus(
            Long userId,
            ApplicationStatus status
    ) {

        return applicationRepository
                .findByUserIdAndStatus(userId, status)
                .stream()
                .map(a ->
                        modelMapper.map(
                                a,
                                ApplicationResponse.class
                        )
                )
                .toList();
    }


    @Override
    public List<ApplicationResponse> getByJobAndStatus(
            Long jobId,
            ApplicationStatus status
    ) {

        return applicationRepository
                .findByJobIdAndStatus(jobId, status)
                .stream()
                .map(a ->
                        modelMapper.map(
                                a,
                                ApplicationResponse.class
                        )
                )
                .toList();
    }

}