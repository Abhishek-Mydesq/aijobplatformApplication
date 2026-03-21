package com.aijobplatform.resume.service.impl;

import com.aijobplatform.kafka.dto.ResumeUploadedEvent;
import com.aijobplatform.resume.client.AiServiceClient;
import com.aijobplatform.resume.client.UserServiceClient;
import com.aijobplatform.resume.common.ApiResponse;
import com.aijobplatform.resume.dto.PageResponse;
import com.aijobplatform.resume.dto.ResumeResponse;
import com.aijobplatform.resume.dto.UserResponse;
import com.aijobplatform.resume.entity.AnalysisStatus;
import com.aijobplatform.resume.entity.Resume;
import com.aijobplatform.resume.entity.ResumeStatus;
import com.aijobplatform.resume.exception.ResourceNotFoundException;
import com.aijobplatform.resume.repository.ResumeRepository;
import com.aijobplatform.resume.service.ResumeService;
import com.aijobplatform.resume.service.async.ResumeAsyncService;
import com.aijobplatform.resume.service.kafka.ResumeEventProducer;

import com.aijobplatform.resume.service.kafka.dto.SearchEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
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
    private final ResumeAsyncService resumeAsyncService;
    private final ResumeEventProducer eventProducer;


    private final String uploadDir =
            System.getProperty("user.dir") + "/uploads/resumes/";


    // ================= UPLOAD =================

    @Override
    public ResumeResponse uploadResume(Long userId, MultipartFile file) {

        ApiResponse<UserResponse> userResponse =
                userServiceClient.getUserById(userId);

        if (userResponse == null ||
                !userResponse.isSuccess() ||
                userResponse.getData() == null) {

            throw new RuntimeException("User not found");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                (!contentType.equals("application/pdf") &&
                        !contentType.equals("application/msword") &&
                        !contentType.equals(
                                "application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {

            throw new RuntimeException("Invalid file type");
        }

        long maxSize = 5 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new RuntimeException("File too large");
        }

        try {

            Path uploadPath =
                    Paths.get(uploadDir).toAbsolutePath().normalize();

            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName =
                    System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );

            long count = resumeRepository.countByUserId(userId);

            Resume resume = Resume.builder()
                    .userId(userId)
                    .fileName(fileName)
                    .fileType(contentType)
                    .filePath(filePath.toString())
                    .fileSize(file.getSize())
                    .version((int) count + 1)
                    .isDefault(count == 0)
                    .status(ResumeStatus.UPLOADED)
                    .analysisStatus(AnalysisStatus.PENDING)
                    .build();

            Resume saved = resumeRepository.save(resume);

            // ✅ Kafka event (use saved)

            ResumeUploadedEvent event = new ResumeUploadedEvent();
            event.setResumeId(saved.getId());
            event.setUserId(saved.getUserId());
            event.setFilePath(saved.getFilePath());

            eventProducer.sendResumeUploaded(event);
            SearchEvent searchEvent =
                    SearchEvent.builder()
                            .refId(saved.getId())
                            .refType("RESUME")
                            .title(saved.getFileName())
                            .content(saved.getFileType())
                            .tags(saved.getStatus().name())
                            .build();

            return modelMapper.map(saved, ResumeResponse.class);

        } catch (IOException e) {
            throw new RuntimeException("Upload failed", e);
        }
    }


    // ================= GET BY USER =================

    @Override
    public PageResponse<ResumeResponse> getResumesByUser(
            Long userId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Resume> resumePage =
                resumeRepository.findByUserId(userId, pageable);

        List<ResumeResponse> responses =
                resumePage.getContent()
                        .stream()
                        .map(r -> modelMapper.map(r, ResumeResponse.class))
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


    // ================= GET BY ID =================

    @Override
    public ResumeResponse getResumeById(Long id) {

        Resume resume =
                resumeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Resume not found"));

        return modelMapper.map(resume, ResumeResponse.class);
    }


    // ================= DELETE =================

    @Override
    public void deleteResume(Long id) {

        Resume resume =
                resumeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Resume not found"));

        resumeRepository.delete(resume);
    }


    // ================= DOWNLOAD =================

    @Override
    public Resource downloadResume(Long id) {

        Resume resume =
                resumeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Resume not found"));

        try {

            Path path =
                    Paths.get(resume.getFilePath()).normalize();

            Resource resource =
                    new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("File not found");
            }

            return resource;

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


    // ================= COUNT =================

    @Override
    public long countByUser(Long userId) {
        return resumeRepository.countByUserId(userId);
    }


    // ================= DEFAULT =================

    @Override
    public ResumeResponse getDefaultResume(Long userId) {

        Resume resume =
                resumeRepository
                        .findByUserIdAndIsDefaultTrue(userId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Default resume not found"));

        return modelMapper.map(resume, ResumeResponse.class);
    }


    @Override
    public void setDefaultResume(Long resumeId) {

        Resume resume =
                resumeRepository.findById(resumeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Resume not found"));

        List<Resume> all =
                resumeRepository
                        .findByUserId(resume.getUserId(), Pageable.unpaged())
                        .getContent();

        for (Resume r : all) {
            r.setIsDefault(false);
        }

        resume.setIsDefault(true);

        resumeRepository.saveAll(all);
    }


    // ================= STATUS =================

    @Override
    public String getResumeStatus(Long resumeId) {

        Resume resume =
                resumeRepository.findById(resumeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Resume not found"));

        return resume.getAnalysisStatus().name();
    }


    // ================= REANALYZE =================

    @Override
    public void reanalyzeResume(Long resumeId) {

        Resume resume =
                resumeRepository.findById(resumeId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Resume not found"));

        resume.setAnalysisStatus(AnalysisStatus.PENDING);

        resumeRepository.save(resume);

        resumeAsyncService.analyzeResume(resumeId);
    }


    // ================= EXISTS =================

    @Override
    public boolean exists(Long resumeId) {
        return resumeRepository.existsById(resumeId);
    }
}