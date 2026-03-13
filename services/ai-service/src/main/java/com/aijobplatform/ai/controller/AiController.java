package com.aijobplatform.ai.controller;

import com.aijobplatform.ai.common.APIResponse;
import com.aijobplatform.ai.dto.AiResumeResponse;
import com.aijobplatform.ai.service.AiResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiResumeService aiResumeService;

    // OLD API (keep for now)
    /*
    @PostMapping("/parse-resume")
    public ResponseEntity<APIResponse<AiResumeResponse>> parseResume(
            @RequestParam("file") MultipartFile file) {

        AiResumeResponse response = aiResumeService.parseResume(file);

        return ResponseEntity.ok(
                new APIResponse<>(true, "Resume analyzed successfully", response)
        );
    }
    */


    // ✅ NEW PRODUCTION API
    @PostMapping("/analyze/{resumeId}")
    public ResponseEntity<APIResponse<AiResumeResponse>> analyzeResume(
            @PathVariable Long resumeId
    ) {

        AiResumeResponse response = aiResumeService.analyzeResume(resumeId);

        return ResponseEntity.ok(
                new APIResponse<>(true, "Resume analyzed successfully", response)
        );
    }

}