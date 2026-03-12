package com.aijobplatform.ai.controller;
import com.aijobplatform.ai.common.APIResponse;
import com.aijobplatform.ai.dto.AiResumeResponse;
import com.aijobplatform.ai.service.AiResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiResumeService aiResumeService;

    @PostMapping("/parse-resume")
    public ResponseEntity<APIResponse<AiResumeResponse>> parseResume(
            @RequestParam("file") MultipartFile file) {
        AiResumeResponse response = aiResumeService.parseResume(file);
        APIResponse<AiResumeResponse> apiResponse =
                new APIResponse<>(true, "Resume analyzed successfully", response);
        return ResponseEntity.ok(apiResponse);
    }
}