package com.aijobplatform.application.service.impl;
import com.aijobplatform.application.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.aijobplatform.application.dto.ApplicationStatus;
import com.aijobplatform.application.dto.ApplyJobRequest;
import com.aijobplatform.application.dto.ApplicationResponse;
import com.aijobplatform.application.entity.Application;
import com.aijobplatform.application.exception.ResourceNotFoundException;
import com.aijobplatform.application.repository.ApplicationRepository;
import com.aijobplatform.application.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;

    @Override
    public ApplicationResponse applyJob(ApplyJobRequest request) {

        if(applicationRepository.existsByUserIdAndJobId(
                request.getUserId(), request.getJobId())) {

            throw new RuntimeException("User already applied to this job");
        }

        Application application = Application.builder()
                .userId(request.getUserId())
                .jobId(request.getJobId())
                .resumeId(request.getResumeId())
                .status(ApplicationStatus.APPLIED)
                .build();

        Application savedApplication = applicationRepository.save(application);

        return modelMapper.map(savedApplication, ApplicationResponse.class);
    }
    @Override
    public PageResponse<ApplicationResponse> getApplicationsByUser(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Application> applicationPage =
                applicationRepository.findByUserId(userId, pageable);

        List<ApplicationResponse> responses = applicationPage.getContent()
                .stream()
                .map(app -> modelMapper.map(app, ApplicationResponse.class))
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
    public PageResponse<ApplicationResponse> getApplicationsByJob(Long jobId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Application> applicationPage =
                applicationRepository.findByJobId(jobId, pageable);

        List<ApplicationResponse> responses = applicationPage.getContent()
                .stream()
                .map(app -> modelMapper.map(app, ApplicationResponse.class))
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
    public ApplicationResponse updateApplicationStatus(Long id, ApplicationStatus status) {
        Application application = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
        application.setStatus(status);
        Application updated = applicationRepository.save(application);
        return modelMapper.map(updated, ApplicationResponse.class);
    }
}