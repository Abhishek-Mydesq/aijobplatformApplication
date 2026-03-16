package com.aijobplatform.resume.controller;

import com.aijobplatform.resume.common.ApiResponse;
import com.aijobplatform.resume.dto.PageResponse;
import com.aijobplatform.resume.dto.ResumeResponse;
import com.aijobplatform.resume.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ResumeResponse>> uploadResume(
            @RequestParam Long userId,
            @RequestParam MultipartFile file
    ) {

        ResumeResponse response = resumeService.uploadResume(userId, file);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Resume uploaded successfully", response)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<PageResponse<ResumeResponse>>> getResumesByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResponse<ResumeResponse> response =
                resumeService.getResumesByUser(userId, page, size);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Resumes fetched successfully", response)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> getResumeById(
            @PathVariable Long id
    ) {

        ResumeResponse response =
                resumeService.getResumeById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Resume fetched successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteResume(
            @PathVariable Long id
    ) {

        resumeService.deleteResume(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Resume deleted successfully", null)
        );
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadResume(@PathVariable Long id) {

        Resource resource = resumeService.downloadResume(id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/user/{userId}/count")
    public ResponseEntity<ApiResponse<Long>> count(
            @PathVariable Long userId
    ) {

        long count = resumeService.countByUser(userId);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Count", count)
        );
    }

    @GetMapping("/user/{userId}/default")
    public ResponseEntity<ApiResponse<ResumeResponse>> getDefault(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Default resume",
                        resumeService.getDefaultResume(userId)
                )
        );
    }

    @PutMapping("/{id}/default")
    public ResponseEntity<ApiResponse<Void>> setDefault(
            @PathVariable Long id
    ) {

        resumeService.setDefaultResume(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Default updated", null)
        );
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<ApiResponse<String>> status(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Status",
                        resumeService.getResumeStatus(id)
                )
        );
    }

    @PostMapping("/{id}/reanalyze")
    public ResponseEntity<ApiResponse<Void>> reanalyze(
            @PathVariable Long id
    ) {

        resumeService.reanalyzeResume(id);

        return ResponseEntity.ok(
                new ApiResponse<>(true, "Reanalyze started", null)
        );
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse<Boolean>> exists(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Exists",
                        resumeService.exists(id)
                )
        );
    }
}