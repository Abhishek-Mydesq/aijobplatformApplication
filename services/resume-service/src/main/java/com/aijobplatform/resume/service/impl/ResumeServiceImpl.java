package com.aijobplatform.resume.service.impl;
import com.aijobplatform.resume.client.AiServiceClient;
import com.aijobplatform.resume.client.UserServiceClient;
import com.aijobplatform.resume.common.ApiResponse;
import com.aijobplatform.resume.dto.PageResponse;
import com.aijobplatform.resume.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.aijobplatform.resume.dto.ResumeResponse;
import com.aijobplatform.resume.entity.Resume;
import com.aijobplatform.resume.exception.ResourceNotFoundException;
import com.aijobplatform.resume.repository.ResumeRepository;
import com.aijobplatform.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final ModelMapper modelMapper;
    private final AiServiceClient aiServiceClient;
    private final UserServiceClient userServiceClient;

    private final String uploadDir = "uploads/resumes/";

    @Override
    public ResumeResponse uploadResume(Long userId, MultipartFile file) {

        ApiResponse<UserResponse> userResponse =
                userServiceClient.getUserById(userId);

        if (userResponse == null ||
                !userResponse.isSuccess() ||
                userResponse.getData() == null) {

            throw new RuntimeException("User not found with id: " + userId);
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("application/pdf") &&
                        !contentType.equals("application/msword") &&
                        !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

            throw new RuntimeException("Invalid file type. Only PDF, DOC, DOCX allowed.");
        }

        long maxSize = 5 * 1024 * 1024;
        if (file.getSize() > maxSize) {
            throw new RuntimeException("File size exceeds 5MB limit");
        }

        try {

            Path uploadPath = Paths.get(uploadDir);

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Resume resume = Resume.builder()
                    .userId(userId)
                    .fileName(fileName)
                    .fileType(contentType)
                    .filePath(filePath.toString())
                    .build();

            Resume savedResume = resumeRepository.save(resume);

            try {
                analyzeResumeAsync(savedResume.getId());
            } catch (Exception e) {
                System.out.println("AI call failed: " + e.getMessage());
            }

            return modelMapper.map(savedResume, ResumeResponse.class);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload resume", e);
        }
    }

    @Override
    public PageResponse<ResumeResponse> getResumesByUser(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Resume> resumePage = resumeRepository.findByUserId(userId, pageable);

        List<ResumeResponse> responses = resumePage.getContent()
                .stream()
                .map(resume -> modelMapper.map(resume, ResumeResponse.class))
                .toList();

        return new PageResponse<>(
                responses,
                resumePage.getNumber(),
                resumePage.getSize(),
                resumePage.getTotalElements(),
                resumePage.getTotalPages(),
                resumePage.isLast()
        );
    }

    @Override
    public ResumeResponse getResumeById(Long id) {

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        return modelMapper.map(resume, ResumeResponse.class);
    }
    @Async
    public void analyzeResumeAsync(Long resumeId) {
        try {
            aiServiceClient.analyzeResume(resumeId);
        } catch (Exception e) {
            System.out.println("AI async failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteResume(Long id) {

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        resumeRepository.delete(resume);
    }

    @Override
    public Resource downloadResume(Long id) {

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        try {

            Path path = Paths.get(resume.getFilePath());

            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error while reading file", e);
        }
    }
}